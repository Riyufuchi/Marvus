package riyufuchi.marvus.tabs;

import java.time.Month;
import java.util.LinkedList;

import riyufuchi.marvus.subTabs.MonthDetail;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;

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
		for (Month month : months)
		{
			categorizedMonths.add(dataSource.getCategorizedMonthByNames(month.getValue()));
			createDataTable(categorizedMonths.getLast().iterator(), month);
		}
	}

	@Override
	protected void showData(int x, int y)
	{
		targetWindow.updateDataDisplayMode(new MonthDetail(targetWindow, categorizedMonths.get(x - 1).get(y), false, this));
	}
}
