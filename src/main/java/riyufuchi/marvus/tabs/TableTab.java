package riyufuchi.marvus.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.dialogs.tools.other.EntityManagerDialog;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.TransactionTableModel;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

/**
 * @author riyufuchi
 * @since 14.11.2024
 * @version 12.12.2024
 */
public class TableTab extends DataDisplayTab
{
	private JComboBox<Month> showForMonth;
	private JComboBox<String> valueFilterOptions, nameOptions, noteOptions, categoryOption;
	private JCheckBox b1, b2, b3, b4;
	private JButton entityManager;
	private LinkedList<Transaction> currDataSet;
	private TransactionTableModel model;
	private JTable table;
	private TableRowSorter<TransactionTableModel> sorter;
	private String string;
	
	public TableTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.valueFilterOptions = SufuFactory.newCombobox(MarvusTexts.VALUE_OPTIONS, evt -> refresh());
		this.showForMonth = SufuFactory.newCombobox(Month.values()); // This combobox must have selected value before action event is assigned otherwise displayed data are duped
		this.nameOptions = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(dataSource.entities.getData()), evt -> refresh());
		this.noteOptions = SufuFactory.newCombobox(MarvusTexts.NOTE_OPTIONS, evt -> refresh());
		this.categoryOption = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(dataSource.categories.getData()), evt -> refresh());
		this.b1 = SufuFactory.newCheckBox("", evt -> checkBoxEvent(showForMonth));
		this.b2 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(nameOptions));
		this.b3 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(noteOptions));
		this.b4 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(categoryOption));
		SufuComponentTools.disableAll(nameOptions, noteOptions, categoryOption);
		SufuComponentTools.centerComboboxList(valueFilterOptions, showForMonth);
		SufuComponentTools.setSelectedItem(showForMonth, LocalDateTime.now().getMonth());
		this.showForMonth.addActionListener(evt -> refresh());
		this.currDataSet = new LinkedList<>();
		this.string = "";
		this.model = new TransactionTableModel(currDataSet);
		this.sorter = new TableRowSorter<>(model);
		this.table = SufuFactory.newTable(model);
		this.table.setRowSorter(sorter);
		this.table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int row = table.rowAtPoint(e.getPoint());
				if (row >= 0) // Ensure a valid cell/row was clicked
				{
					showExtednedInfo(model.getTransactionAt(table.convertRowIndexToModel(row)), e);
				}
			}
		});
		this.entityManager = SufuFactory.newButton("Entity manager", evt -> { 
			new EntityManagerDialog((MarvusDataWindow)targetWindow.getSelf()).showDialog();
			nameOptions.removeAllItems();
			for (String item : MarvusTableUtils.selectOrdered(dataSource.entities.getData()))
				nameOptions.addItem(item);
			categoryOption.removeAllItems();
			for (String item : MarvusTableUtils.selectOrdered(dataSource.categories.getData()))
				categoryOption.addItem(item);
		});
		addMenuAndMenuItems(entityManager, b2, nameOptions, b4, categoryOption, valueFilterOptions, b1, showForMonth, b3, noteOptions);
		masterPanel.simulateBorderLayout();
		masterPanel.add(new JScrollPane(table), masterPanel.getGBC(0, 1));
	}
	
	private void filterData()
	{	
		if (showForMonth.isEnabled())
		{
			currDataSet = dataSource.getMonth(SufuComponentTools.extractComboboxValue(showForMonth).getValue() - 1);
		}
		else
		{
			currDataSet = new LinkedList<>(dataSource.toList());
		}
		// Filtering
		if (nameOptions.isEnabled())
		{
			string = SufuComponentTools.<String>extractComboboxValue(nameOptions);
			currDataSet.removeIf(t -> !t.getName().equals(string));
		}
		
		if (categoryOption.isEnabled())
		{
			string = SufuComponentTools.<String>extractComboboxValue(categoryOption);
			currDataSet.removeIf(t -> !t.getCategory().equals(string));
		}

		if (noteOptions.isEnabled())
		{
			if (noteOptions.getSelectedIndex() == 0)
				currDataSet.removeIf(t -> t.getNote().isBlank());
			else
				currDataSet.removeIf(t -> !t.getNote().isBlank());
		}

		if (valueFilterOptions.isEnabled() && valueFilterOptions.getSelectedIndex() > 0)
		{
			if (valueFilterOptions.getSelectedIndex() == 1)
				currDataSet.removeIf(t -> t.getValue().compareTo(BigDecimal.ZERO) <= 0);
			else if (valueFilterOptions.getSelectedIndex() == 2)
				currDataSet.removeIf(t -> t.getValue().compareTo(BigDecimal.ZERO) >= 0);
		}
		
		if (currDataSet.isEmpty())
			SufuDialogHelper.informationDialog(targetWindow.getSelf(), "No data to display with current settings.", "Filtering result");
	}
	
	private void checkBoxEvent(JComponent comp)
	{
		comp.setEnabled(!comp.isEnabled());
		refresh();
	}
	
	// Overrides

	@Override
	public void displayData()
	{
		refresh();
	}

	@Override
	public void refresh()
	{
		filterData();
		model = new TransactionTableModel(currDataSet);
		sorter = new TableRowSorter<>(model);
		table.setModel(model);
		table.setRowSorter(sorter);
		SufuComponentTools.resizeColumnWidths(table);
	}
}
