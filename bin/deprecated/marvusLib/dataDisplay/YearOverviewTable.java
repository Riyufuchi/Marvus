package riyufuchi.marvus.marvusLib.dataDisplay;

import java.math.BigDecimal;
import java.time.Month;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvus.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 1.66 - 05.09.2023
 * @version 2.0 - 09.02.2024
 */
// TODO: Optimize this class for refresh method
public class YearOverviewTable extends DataDisplayMode
{
	private YearOverview yearOverview;
	
	public YearOverviewTable(MarvusDataFrame targetWindow, int year)
	{
		super(targetWindow);
		this.yearOverview = dataSource.getYearOverview(year);
	}

	@Override
	public void displayData()
	{
		buildTable(yearOverview, targetWindow, 0, 0, true);
	}
	
	public static void buildTable(YearOverview yearOverview, MarvusDataFrame targetWindow, int baseX, int baseY, boolean addHeader)
	{
		if (yearOverview == null)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), "No input data!", "Data IO error");
			return;
		}
		BigDecimal[] income = yearOverview.income();
		BigDecimal[] spendings = yearOverview.spendigs();
		JPanel panel = targetWindow.getPane();
		SufuTableTools.addColumnHeader(targetWindow, baseX++, baseY, String.valueOf(yearOverview.year()), "Income", "Spendings", "Outcome");
		if (addHeader)
			SufuTableTools.addRowHeader(targetWindow, baseX, baseY, Month.values());
		int xPos = 0;
		int incomeY = baseY + 1;
		int spendigsY = incomeY + 1;
		int outcomeY = spendigsY + 1;
		for (int x = baseX; x < 13; x++)
		{
			panel.add(SufuFactory.newTextFieldHeader(income[xPos].toString()), targetWindow.getGBC(x, incomeY));
			panel.add(SufuFactory.newTextFieldHeader(spendings[xPos].toString()), targetWindow.getGBC(x, spendigsY));
			panel.add(SufuFactory.newTextFieldHeader((income[xPos].add(spendings[xPos]).toString())), targetWindow.getGBC(x, outcomeY)); // outcome is already negative
			xPos++;
		}
		if (addHeader)
			SufuTableTools.addColumnHeader(targetWindow, 13, baseY, "Year total");
		SufuTableTools.addColumnHeader(targetWindow, 13, ++baseY, yearOverview.totalIncome(), yearOverview.totalOutcome(), yearOverview.totalResult());
	}

	@Override
	public void refresh()
	{
		yearOverview = dataSource.getYearOverview(yearOverview.year());
		hardRefresh();
	}
}
