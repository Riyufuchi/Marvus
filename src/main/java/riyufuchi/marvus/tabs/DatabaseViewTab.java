package riyufuchi.marvus.tabs;

import java.time.LocalDateTime;
import java.time.Month;

import javax.swing.JComboBox;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
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
	
	public DatabaseViewTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
	}
	
	// Comparators
	
	private void monthOnly()
	{
		clearPanel(masterPanel, MarvusTexts.TRANSACTION_VIEWER_HEADER.length - 1);
		int y = 1;
		Month e = SufuComponentTools.extractComboboxValue(showForMonth);
		for (Transaction t : dataSource)
		{
			if (t.getDate().getMonth() == e)
			{
				SufuTableTools.<Object>addRowHeader(masterPanel, 1, y, t.getName(), t.getCategory(), t.getValue(), t.getCurrency(), t.getStringDate(), t.getNote());
				masterPanel.add(SufuFactory.newTextFieldCell(String.valueOf(t.getID()), evt2 -> showExtednedInfo(t, evt2)), masterPanel.getGBC(0, y));
				y++;
			}
		}
		masterPanel.revalidate();
		masterPanel.repaint();
	}


	@Override
	public void prepareUI()
	{
		showForMonth = SufuFactory.newCombobox(Month.values(), evt -> monthOnly());
		SufuComponentTools.centerComboboxList(showForMonth);
		addMenuAndMenuItems(showForMonth);
		SufuTableTools.addRowHeader(masterPanel, 0, 0, MarvusTexts.TRANSACTION_VIEWER_HEADER);
		SufuComponentTools.setSelectedItem(showForMonth, LocalDateTime.now().getMonth());
	}

	@Override
	public void displayData()
	{
	}

	@Override
	public void refresh()
	{
		monthOnly();
	}
}
