package riyufuchi.marvus.tabs;

import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedMonthOverview extends CategorizedMonthList
{
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	
	public CategorizedMonthOverview(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
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
		Iterator<FinancialCategory> it = null;
		for (Month month : months) // for (int month = 0; month < 12; month++)
		{
			categorizedMonths.add(dataSource.getCategorizedMonthByNames(month.getValue()));
			it = categorizedMonths.getLast().iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), MarvusGuiUtils.encodeCords(month.getValue(), y - 1), evt -> btnDataReference(evt)), targetWindow.getGBC(month.getValue(), y));
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
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, categorizedMonths.get(x - 1).get(y), false));
	}
}
