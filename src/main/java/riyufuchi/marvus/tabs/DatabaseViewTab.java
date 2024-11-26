package riyufuchi.marvus.tabs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author riyufuchi
 * @since 14.11.2024
 * @version 26.11.2024
 */
public class DatabaseViewTab extends DataDisplayTab
{
	private JComboBox<Month> showForMonth;
	private JComboBox<String> valueFilterOptions, nameOptions, noteOptions, categoryOption;
	private JCheckBox b1, b2, b3, b4;
	private LinkedList<Transaction> currDataSet;
	private int y;
	
	public DatabaseViewTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.currDataSet = new LinkedList<>();
		this.y = 1;
		this.valueFilterOptions = SufuFactory.newCombobox(MarvusTexts.VALUE_OPTIONS, evt -> refresh());
		this.showForMonth = SufuFactory.newCombobox(Month.values()); // This combobox must have selected value before action event is assigned otherwise displayed data are duped
		this.nameOptions = SufuFactory.newCombobox(MarvusDatabase.utils.getNames(), evt -> refresh());
		this.noteOptions = SufuFactory.newCombobox(MarvusTexts.NOTE_OPTIONS, evt -> refresh());
		this.categoryOption = SufuFactory.newCombobox(MarvusDatabase.utils.getCategoryEnum(), evt -> refresh());
		this.b1 = SufuFactory.newCheckBox("", evt -> checkBoxEvent(showForMonth));
		this.b2 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(nameOptions));
		this.b3 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(noteOptions));
		this.b4 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(categoryOption));
		disable(nameOptions, noteOptions, categoryOption);
		SufuComponentTools.centerComboboxList(valueFilterOptions, showForMonth);
		SufuComponentTools.setSelectedItem(showForMonth, LocalDateTime.now().getMonth());
		showForMonth.addActionListener(evt -> refresh());
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
			String name = SufuComponentTools.<String>extractComboboxValue(nameOptions);
			currDataSet.removeIf(t -> !t.getName().equals(name));
		}
		
		if (categoryOption.isEnabled())
		{
			String category = SufuComponentTools.<String>extractComboboxValue(categoryOption);
			currDataSet.removeIf(t -> !t.getCategory().equals(category));
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
			BigDecimal zero = BigDecimal.ZERO;
			if (valueFilterOptions.getSelectedIndex() == 1)
				currDataSet.removeIf(t -> t.getValue().compareTo(zero) <= 0);
			else if (valueFilterOptions.getSelectedIndex() == 2)
				currDataSet.removeIf(t -> t.getValue().compareTo(zero) >= 0);
		}
		
		if (currDataSet.isEmpty())
			SufuDialogHelper.informationDialog(targetWindow.getSelf(), "No data to display with current settings.", "Filtering result");
	}
	
	private void checkBoxEvent(JComponent comp)
	{
		comp.setEnabled(!comp.isEnabled());
		refresh();
	}
	
	private void disable(JComponent ... evt)
	{
		for (JComponent c : evt)
			c.setEnabled(false);
	}
	
	// Overrides

	@Override
	public void prepareUI()
	{
		addMenuAndMenuItems(b2, nameOptions, b4, categoryOption, valueFilterOptions, b1, showForMonth, b3, noteOptions);
		SufuTableTools.addRowHeader(masterPanel, 0, 0, MarvusTexts.TRANSACTION_VIEWER_HEADER);
	}

	@Override
	public void displayData()
	{
		filterData();
		masterPanel.revalidate();
		SwingUtilities.invokeLater(() -> {
			for (Transaction t : currDataSet)
			{
				SufuTableTools.<Object>addRowHeader(masterPanel, 1, y, t.getName(), t.getCategory(), t.getValue(), t.getCurrency(), t.getStringDate(), t.getNote());
				masterPanel.add(SufuFactory.newTextFieldCell(String.valueOf(t.getID()), evt2 -> showExtednedInfo(t, evt2)), masterPanel.getGBC(0, y));
				y++;
			}
			masterPanel.revalidate();
		});
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, MarvusTexts.TRANSACTION_VIEWER_HEADER.length - 1);
		y = 1;
		displayData();
	}
}
