package riyufuchi.marvus.tabs;

import java.time.Month;

import riyufuchi.marvus.tabs.subTabs.TableDetail;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;

/**
 * Uncategorized month list originally called CategorizedMonthOverview
 */
public class UncategorizedMonthListTab extends CategorizedMonthListTab
{
	public UncategorizedMonthListTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
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
		targetWindow.updateDataDisplayMode(new TableDetail(targetWindow, categorizedMonths.get(x - 1).get(y), this));
	}
}
