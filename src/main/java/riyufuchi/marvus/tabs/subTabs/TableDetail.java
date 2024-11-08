package riyufuchi.marvus.tabs.subTabs;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 08.11.2024
 * @version 08.11.2024
 */
public class TableDetail extends DataDisplayTab
{
	private Iterable<Transaction> transactions;
	
	public TableDetail(MarvusTabbedFrame targetWindow, Iterable<Transaction> iterable, DataDisplayTab parentTab)
	{
		super(targetWindow, null, parentTab);
		this.transactions = iterable;
	}

	@Override
	public void prepareUI()
	{
		SufuTableTools.addRowHeader(targetWindow, 0, 0, MarvusTexts.TRANSACTION_VIEWER_HEADER);
	}

	@Override
	public void displayData()
	{
		int y = 1;
		for (Transaction t : transactions)
		{
			SufuTableTools.<Object>addRowHeader(targetWindow, 0, y, t.getID(), t.getName(), t.getCategory(), t.getValue(), t.getCurrency(), t.getStringDate(), t.getNote());
			y++;
		}
	}

	@Override
	public void refresh()
	{
		targetWindow.updateDataDisplayMode(getSuperTab());
	}
}
