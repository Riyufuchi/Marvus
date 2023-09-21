package riyufuchi.marvus.marvusLib.dataDisplay;

import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

public class CategorizedMonthOverview extends CategorizedMonthList
{
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	
	public CategorizedMonthOverview(MarvusDataFrame targetWindow, TransactionDataTable dataSource)
	{
		super(targetWindow, dataSource);
		this.categorizedMonths = new LinkedList<>();
	}

	@Override
	public void displayData()
	{
		JPanel pane = targetWindow.getPane();
		int y = 1;
		FinancialCategory fc = null;
		SufuWindowTools.createTableRowHeader(targetWindow, 0, Month.values());
		for (int month = 0; month < 12; month++)
		{
			categorizedMonths.add(dataSource.getCategorizedMonthByCategory(month));
			Iterator<FinancialCategory> it = categorizedMonths.getLast().iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), createBtnName(month, y), evt -> btnDataReference(evt)), targetWindow.getGBC(month, y));
				y++;
			}
			y = 1;
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}

	@Override
	protected void showData(int x, int y)
	{
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, dataSource, categorizedMonths.get(x).get(y), false));
	}
}
