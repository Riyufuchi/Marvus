package riyufuchi.marvus.tabs.viewTabs;

import java.time.Month;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.TableDetail;

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
			categorizedMonths.add(database.getCategorizedMonthByNames(month));
			createDataTable(categorizedMonths.getLast().iterator(), month);
		}
	}

	@Override
	protected void showData(int x, int y)
	{
		targetWindow.updateDataDisplayMode(new TableDetail(targetWindow, categorizedMonths.get(x - 1).get(y), this));
	}
}
