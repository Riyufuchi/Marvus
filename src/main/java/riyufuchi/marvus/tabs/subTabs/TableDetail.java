package riyufuchi.marvus.tabs.subTabs;

import java.util.LinkedList;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 08.11.2024
 * @version 12.11.2024
 */
public class TableDetail extends DataDisplayTab
{
	private LinkedList<Iterable<Transaction>> transactions;
	private int y;
	
	public TableDetail(MarvusTabbedFrame targetWindow, Iterable<Transaction> iterable, DataDisplayTab parentTab)
	{
		super(targetWindow, null, parentTab);
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
