package riyufuchi.marvus.tabs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author riyufuchi
 * @since 14.11.2024
 * @version 14.11.2024
 */
public class DatabaseViewTab extends DataDisplayTab
{
	private JComboBox<Month> showForMonth;
	private JComboBox<String> valueFilterOptions, nameOptions, noteOptions;
	private LinkedList<Transaction> currDataSet;
	private int y;
	
	public DatabaseViewTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.currDataSet = new LinkedList<>();
		this.y = 1;
	}
	
	private void filterData()
	{
		LinkedList<Transaction> newList = new LinkedList<>();
		
		if (showForMonth.isEnabled())
		{
			currDataSet = dataSource.getMonth(SufuComponentTools.extractComboboxValue(showForMonth).getValue() - 1);
		}
		else
		{
			currDataSet = dataSource.toList();
		}
		
		if (nameOptions.isEnabled())
		{
			newList.clear();
			String name = SufuComponentTools.<String>extractComboboxValue(nameOptions);
			for (Transaction t : currDataSet)
				if (t.getName().equals(name))
					newList.add(t);
			currDataSet = newList;
		}
		
		if (noteOptions.isEnabled())
		{
			newList.clear();
			if (noteOptions.getSelectedIndex() == 0)
			{
				for (Transaction t : currDataSet)
					if (!t.getNote().isBlank())
						newList.add(t);	
			}
			else
			{
				for (Transaction t : currDataSet)
					if (t.getNote().isBlank())
						newList.add(t);
			}
			currDataSet = newList;
		}
		
		if (valueFilterOptions.isEnabled() && valueFilterOptions.getSelectedIndex() != 0)
		{
			newList.clear();
			BigDecimal zero = new BigDecimal(0);
			if (valueFilterOptions.getSelectedIndex() == 1)
			{
				for (Transaction t : currDataSet)
					if (t.getValue().compareTo(zero) > 0)
						newList.add(t);
			}
			else if (valueFilterOptions.getSelectedIndex() == 2)
			{
				for (Transaction t : currDataSet)
					if (t.getValue().compareTo(zero) < 0)
						newList.add(t);
			}
			currDataSet = newList;
		}
	}
	
	private void checkBoxEvent(JComponent comp)
	{
		comp.setEnabled(!comp.isEnabled());
		refresh();
	}
	
	// Overrides

	@Override
	public void prepareUI()
	{
		valueFilterOptions = SufuFactory.newCombobox(MarvusTexts.VALUE_OPTIONS, evt -> refresh());
		showForMonth = SufuFactory.newCombobox(Month.values(), evt -> refresh());
		nameOptions = SufuFactory.newCombobox(MarvusDatabase.utils.getNames(), evt -> refresh());
		noteOptions = SufuFactory.newCombobox(MarvusTexts.NOTE_OPTIONS, evt -> refresh());
		JCheckBox b1 = SufuFactory.newCheckBox("", evt -> checkBoxEvent(showForMonth));
		JCheckBox b2 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(nameOptions));
		JCheckBox b3 = SufuFactory.newCheckBox("", true, evt -> checkBoxEvent(noteOptions));
		nameOptions.setEnabled(false);
		noteOptions.setEnabled(false);
		SufuComponentTools.centerComboboxList(valueFilterOptions, showForMonth);
		addMenuAndMenuItems(b2, nameOptions, valueFilterOptions, b1, showForMonth, b3, noteOptions);
		SufuTableTools.addRowHeader(masterPanel, 0, 0, MarvusTexts.TRANSACTION_VIEWER_HEADER);
		SufuComponentTools.setSelectedItem(showForMonth, LocalDateTime.now().getMonth());
	}

	@Override
	public void displayData()
	{
		filterData();
		for (Transaction t : currDataSet)
		{
			SufuTableTools.<Object>addRowHeader(masterPanel, 1, y, t.getName(), t.getCategory(), t.getValue(), t.getCurrency(), t.getStringDate(), t.getNote());
			masterPanel.add(SufuFactory.newTextFieldCell(String.valueOf(t.getID()), evt2 -> showExtednedInfo(t, evt2)), masterPanel.getGBC(0, y));
			y++;
		}
		masterPanel.revalidate();
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, MarvusTexts.TRANSACTION_VIEWER_HEADER.length - 1);
		displayData();
	}
}
