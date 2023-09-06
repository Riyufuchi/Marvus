package riyufuchi.marvus.marvusLib.dataDisplay;

import java.math.BigDecimal;
import java.time.Month;

import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.marvusLib.financialRecords.YearOverview;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/*
 * Created On: 05.09.2023
 * Last Edit: 06.09.2023
 */
/**
 * @author Riyufuchi
 * @version 1.1
 * @since 0.1.66
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
		SufuWindowTools.createTableColumn(targetWindow, 0, String.valueOf(yearOverview.year()), "Income", "Outcome", "Total");
		//Month[] months = Month.values();
		SufuWindowTools.<Month>createTableRow(targetWindow, 1, 0, Month.values());
		int xPos = 0;
		for (int x = 1; x < 13; x++)
		{
			//panel.add(SufuFactory.newTextFieldCell(months[xPos].toString()), targetWindow.getGBC(x, 0));
			panel.add(SufuFactory.newTextFieldCell(income[xPos].toString()), targetWindow.getGBC(x, 1));
			panel.add(SufuFactory.newTextFieldCell(outcome[xPos].toString()), targetWindow.getGBC(x, 2));
			panel.add(SufuFactory.newTextFieldCell((income[xPos].add(outcome[xPos]).toString())), targetWindow.getGBC(x, 3)); // outcome is already negative
			xPos++;
		}
		income = yearOverview.getTotalData();
		SufuWindowTools.createTableColumn(targetWindow, 13 , "Year total", income[0], income[1], income[2]);
	}

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
