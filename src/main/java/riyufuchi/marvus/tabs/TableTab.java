package riyufuchi.marvus.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.dialogs.tools.other.EntityManagerDialog;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.MarvusDataComparation;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.marvusLib.database.TransactionTableModel;
import riyufuchi.marvusLib.enums.MarvusTransactionOrderBy;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.gui.utils.SufuFactory;

/**
 * @author riyufuchi
 * @since 14.11.2024
 * @version 15.01.2025
 */
public class TableTab extends DataDisplayTab
{
	private JComboBox<Month> showForMonth;
	private JComboBox<String> valueFilterOptions, nameOptions, noteOptions, categoryOption;
	private JComboBox<MarvusTransactionOrderBy> orderByComboBox;
	private JSpinner dayOption;
	private JCheckBox b1, b2, b3, b4, b5;
	private JButton entityManager;
	private LinkedList<Transaction> currDataSet;
	private TransactionTableModel model;
	private JTable table;
	private TableRowSorter<TransactionTableModel> sorter;
	private Comparator<Transaction> dataSorter;
	private String string;
	
	public TableTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.valueFilterOptions = SufuFactory.newCombobox(MarvusTexts.VALUE_OPTIONS, evt -> refresh());
		this.showForMonth = SufuFactory.newCombobox(Month.values()); // This combobox must have selected value before action event is assigned otherwise displayed data are duped
		this.nameOptions = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.getEntitiesTable().getData()), evt -> refresh());
		this.noteOptions = SufuFactory.newCombobox(MarvusTexts.NOTE_OPTIONS, evt -> refresh());
		this.categoryOption = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.getCategoriesTable().getData()), evt -> refresh());
		this.orderByComboBox = SufuFactory.newCombobox(MarvusTransactionOrderBy.values(), evt -> {
			dataSorter = MarvusDataComparation.compareBy(SufuComponentTools.extractComboboxValue(orderByComboBox));
			refresh();
		});
		this.dataSorter = MarvusDataComparation.compareBy(MarvusTransactionOrderBy.OLDEST_TO_NEWEST);
		this.dayOption = SufuFactory.newSpinner(1, 1, 31, 1, evt -> refresh());
		this.b1 = SufuFactory.newCheckBox("", evt -> checkBoxEvent(showForMonth));
		this.b2 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(nameOptions));
		this.b3 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(noteOptions));
		this.b4 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(categoryOption));
		this.b5 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(dayOption));
		SufuComponentTools.disableAll(nameOptions, noteOptions, categoryOption, dayOption);
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
			updateCB(nameOptions, database.getEntitiesTable().getData());
			updateCB(categoryOption, database.getCategoriesTable().getData());
		});
		addMenuAndMenuItems(entityManager, b2, nameOptions, b4, categoryOption, valueFilterOptions, b5, dayOption, b1, showForMonth, b3, noteOptions, orderByComboBox);
		masterPanel.simulateBorderLayout();
		masterPanel.add(new JScrollPane(table), masterPanel.getGBC(0, 1));
	}
	
	private void updateCB(JComboBox<String> cb, List<String> data)
	{
		boolean enabled = cb.isEnabled();
		int selectedIndex = cb.getSelectedIndex();
		cb.setEnabled(false);
		cb.removeAllItems();
		for (String item : MarvusTableUtils.selectOrdered(data))
			cb.addItem(item);
		cb.setEnabled(enabled);
		cb.setSelectedIndex(selectedIndex);
	}
	
	private void filterData()
	{	
		if (showForMonth.isEnabled())
		{
			currDataSet = database.getMonth(SufuComponentTools.extractComboboxValue(showForMonth));
		}
		else
		{
			currDataSet = new LinkedList<>(database.getTransactionsTable().getData());
		}
		// Filtering
		
		if (dayOption.isEnabled())
		{
			int day = (int)dayOption.getValue();
			currDataSet.removeIf(t -> !(t.getDate().getDayOfMonth() == day));
		}
		
		if (nameOptions.isEnabled())
		{
			string = SufuComponentTools.<String>extractComboboxValue(nameOptions);
			if (string.equals("Custom"))
				for (String name : database.getEntitiesTable().getData())
					currDataSet.removeIf(t -> t.getName().equals(name));
			else
				currDataSet.removeIf(t -> !t.getName().equals(string));
		}
		
		if (categoryOption.isEnabled())
		{
			string = SufuComponentTools.<String>extractComboboxValue(categoryOption);
			if (string.equals("Custom"))
				for (String name : database.getCategoriesTable().getData())
					currDataSet.removeIf(t -> t.getCategory().equals(name));
			else
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
		
		currDataSet.sort(dataSorter);
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
