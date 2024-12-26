package riyufuchi.marvus.tabs;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 1.66 - 05.09.2023
 * @version 26.12.2024
 */
public class YearOverviewTab extends DataDisplayTab
{
	private final int OFFSET = 5;
	private final int NUM_OF_GENENERATED_COLUMNS = 13;
	private final DecimalFormat DF = new DecimalFormat("0.00");
	private LinkedList<YearOverview> yearOverviews;
	private int yOffset;
	private int year;
	
	public YearOverviewTab(MarvusTabbedFrame targetWindow, int year)
	{
		super(targetWindow);
		this.yOffset = 0;
		this.year = year;
		this.yearOverviews = new LinkedList<>();
		this.yearOverviews.add(null);
		// UI
		addMenuAndMenuItems(SufuFactory.newButton("Add table", evt -> addTable()),
				SufuFactory.newButton("Export to CSV", evt -> exportToCsv()));
		addContentPanel();
		buildTableHeader();
	}
	
	private BigDecimal[] absArr(BigDecimal[] arr)
	{
		for (int i = 0; i < arr.length; i++)
			arr[i] = arr[i].abs();
		return arr;
	}
	
	private void exportToCsv()
	{
		YearOverview yo = yearOverviews.getFirst();
		LinkedList<String> rows = new LinkedList<>();
		rows.add(buildTableHeaderCsv(String.valueOf(yo.year())));
		rows.add(formarRow("Income", yo.income()));
		rows.add(formarRow("Spendings", absArr(yo.spendigs())));
		rows.add(formarRow("Outcome", yo.outcomes()));
		String path = "No file was selected.";
		try
		{
			path = MarvusGuiUtils.pathSelector(targetWindow.getSelf());
		}
		catch (NoSuchElementException e)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), path + "\nException detail: " + e.getLocalizedMessage(), e.getClass().getSimpleName());
			return;
		}
		try
		{
			SufuPersistence.saveToCSVtoString(path, rows);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(targetWindow.getSelf(), e);
		}
	}
	
	public String buildTableHeaderCsv(String year)
	{
		LinkedList<String> header = new LinkedList<>();
		for (Month month : Month.values())
		{
			header.add(month.name()); // Add each month from the Month enum
		}
		StringBuilder result = new StringBuilder(year);
		for (String s : header)
			result.append(";").append(s);
		
		return result.toString();
	}
	
	public String formarRow(String name, BigDecimal[] bgs)
	{
		StringBuilder result = new StringBuilder(name);
		for (BigDecimal value : bgs)
		{
			result.append(";").append(value.toPlainString());
		}
		return result.toString();
	}
	
	private void addTable()
	{
		String path = "No file was selected.";
		try
		{
			path = MarvusGuiUtils.pathSelector(targetWindow.getSelf());
		}
		catch (NoSuchElementException e)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), path + "\nException detail: " + e.getLocalizedMessage(), e.getClass().getSimpleName());
			return;
		}
		MarvusDatabase db = null;
		try
		{
			db = MarvusIO.inputFile(path).convertDataToDB();
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(targetWindow.getSelf(), e);
			return;
		}
		yearOverviews.add(db.getYearOverview(db.assumeYear()));
		targetWindow.refresh();
	}
	
	private void buildTableHeader()
	{
		SufuTableTools.addRowHeader(contentPanel, 1, 0, Month.values());
		SufuTableTools.addColumnHeader(contentPanel, NUM_OF_GENENERATED_COLUMNS, 0, "YEAR TOTAL");
	}
	
	private void buildTable(YearOverview yearOverview, int baseX, int baseY)
	{
		if (yearOverview == null)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), "No data inputed!", "Data IO error");
			return;
		}
		BigDecimal[] income = yearOverview.income();
		BigDecimal[] spendings = yearOverview.spendigs();
		BigDecimal[] totals = yearOverview.totals();
		SufuTableTools.addColumnHeader(contentPanel, baseX++, baseY, String.valueOf(yearOverview.year()), "Income", "Spendings", "Outcome", "Total");
		int xPos = 0;
		int incomeY = baseY + 1;
		int spendigsY = incomeY + 1;
		int outcomeY = spendigsY + 1;
		int totalY = outcomeY + 1;
		for (int x = baseX; x < NUM_OF_GENENERATED_COLUMNS; x++)
		{
			contentPanel.add(SufuFactory.newTextFieldHeader(DF.format(income[xPos])), contentPanel.getGBC(x, incomeY));
			contentPanel.add(SufuFactory.newTextFieldHeader(DF.format(spendings[xPos].abs())), contentPanel.getGBC(x, spendigsY));
			contentPanel.add(SufuFactory.newTextFieldHeader(DF.format(income[xPos].add(spendings[xPos]))), contentPanel.getGBC(x, outcomeY));
			contentPanel.add(SufuFactory.newTextFieldHeader(DF.format(totals[xPos])), contentPanel.getGBC(x, totalY));
			xPos++;
		}
		SufuTableTools.addColumnHeader(contentPanel, NUM_OF_GENENERATED_COLUMNS, ++baseY, DF.format(yearOverview.totalIncome()), DF.format(yearOverview.totalSpendings().abs()), DF.format(yearOverview.totalResult()));
	}
	
	// OVERRIDES
	
	@Override
	public void displayData()
	{
		refresh();
	}
	
	@Override
	public void refresh()
	{
		yearOverviews.set(0, dataSource.getYearOverview(year));
		clearPanel(contentPanel, NUM_OF_GENENERATED_COLUMNS);
		yOffset = 0;
		for (YearOverview yearOverview : yearOverviews)
		{
			buildTable(yearOverview, 0, yOffset);
			yOffset += OFFSET;
		}
	}
}
