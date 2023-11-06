package riyufuchi.marvus.marvusLib.dataDisplay;

import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedMonthOverview extends CategorizedMonthList
{
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	
	public CategorizedMonthOverview(MarvusDataFrame targetWindow, MarvusDatabase dataSource)
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
		Month[] months = Month.values();
		SufuTableTools.addRowHeader(targetWindow, 1, 0, months); // Because January is 1 in enumeration Month
		//for (int month = 0; month < 12; month++)
		for (Month month : months)
		{
			categorizedMonths.add(dataSource.getCategorizedMonth(month));
			Iterator<FinancialCategory> it = categorizedMonths.getLast().iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), createBtnName(month.getValue(), y), evt -> btnDataReference(evt)), targetWindow.getGBC(month.getValue(), y));
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
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, dataSource, categorizedMonths.get(x - 1).get(y), false));
	}
}
