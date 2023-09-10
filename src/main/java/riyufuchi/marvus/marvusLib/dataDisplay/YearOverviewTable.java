package riyufuchi.marvus.marvusLib.dataDisplay;

import java.math.BigDecimal;
import java.time.Month;

import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.marvusLib.dataStorage.CategoryYearTable;
import riyufuchi.marvus.marvusLib.financialRecords.YearOverview;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * @author Riyufuchi
 * @version 1.3 - Last Edit: 08.09.2023
 * @since 0.1.66 - Created On: 05.09.2023
 */
// TODO: Optimize this class for refresh method
public class YearOverviewTable extends DataDisplayMode
{
	private YearOverview yearOverview;
	
	public YearOverviewTable(SufuWindow targetWindow, CategoryYearTable source, int year)
	{
		super(targetWindow, source);
		this.yearOverview = dataSource.getYearOverview(year);
	}

	@Override
	public void displayData()
	{
		BigDecimal[] income = yearOverview.income();
		BigDecimal[] outcome = yearOverview.outcome();
		JPanel panel = targetWindow.getPane();
		SufuWindowTools.createTableColumnHeader(targetWindow, 0, String.valueOf(yearOverview.year()), "Income", "Outcome", "Total");
		SufuWindowTools.<Month>createTableRowHeader(targetWindow, 1, 0, Month.values());
		int xPos = 0;
		for (int x = 1; x < 13; x++)
		{
			panel.add(SufuFactory.newTextFieldHeader(income[xPos].toString()), targetWindow.getGBC(x, 1));
			panel.add(SufuFactory.newTextFieldHeader(outcome[xPos].toString()), targetWindow.getGBC(x, 2));
			panel.add(SufuFactory.newTextFieldHeader((income[xPos].add(outcome[xPos]).toString())), targetWindow.getGBC(x, 3)); // outcome is already negative
			xPos++;
		}
		SufuWindowTools.createTableColumnHeader(targetWindow, 13 , "Year total", yearOverview.totalIncome(), yearOverview.totalOutcome(), yearOverview.totalResult());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void refresh()
	{
		yearOverview = dataSource.getYearOverview(yearOverview.year());
		BigDecimal[] income = yearOverview.income();
		BigDecimal[] outcome = yearOverview.outcome();
		for (int x = 0; x < 12; x++)
		{
			((JTextField)targetWindow.getComponent(x+1, 2+1)).setText(income[x].toString());
			((JTextField)targetWindow.getComponent(x, 3)).setText(outcome[x].toString());
			((JTextField)targetWindow.getComponent(x, 4)).setText((income[x].add(outcome[x]).toString()));
		}
		income = yearOverview.getTotalData();
		((JTextField)targetWindow.getComponentAt(13, 1)).setText(income[0].toString());
		((JTextField)targetWindow.getComponentAt(13, 2)).setText(income[1].toString());
		((JTextField)targetWindow.getComponentAt(13, 2)).setText((income[2].toString()));
		//targetWindow.refreshWindow();
	}
}
