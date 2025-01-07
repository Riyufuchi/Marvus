package riyufuchi.marvus.tabs.subTabs;

import java.util.LinkedList;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 08.11.2024
 * @version 31.12.2024
 */
public class TableDetail extends DataDisplayTab
{
	private LinkedList<Iterable<Transaction>> transactions;
	private int y;
	
	public TableDetail(MarvusTabbedFrame targetWindow, Iterable<Transaction> iterable, DataDisplayTab parentTab)
	{
		super(targetWindow, parentTab.getDataSource(), parentTab);
		this.transactions = new LinkedList<>();
		transactions.add(iterable);
		this.y = 1;
	}
	
	public void addData(Iterable<Transaction> it)
	{
		transactions.add(it);
	}

	@Override
	public void prepareUI()
	{
		SufuTableTools.addRowHeader(targetWindow, 0, 0, MarvusTexts.TRANSACTION_VIEWER_HEADER);
	}

	@Override
	public void displayData()
	{
		for (Iterable<Transaction> it : transactions)
			for (Transaction t : it)
			{
				SufuTableTools.<Object>addRowHeader(targetWindow, 1, y, t.getName(), t.getCategory(), t.getValue(), t.getCurrency(), t.getStringDate(), t.getNote());
				masterPanel.add(SufuFactory.newTextFieldCell(String.valueOf(t.getID()), evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, y));
				y++;
			}
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, MarvusTexts.TRANSACTION_VIEWER_HEADER.length - 1);
		displayData();
	}
}
