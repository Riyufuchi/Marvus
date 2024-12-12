package riyufuchi.marvus.tabs;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 1.66 - 05.09.2023
 * @version 12.12.2024
 */
public class YearOverviewTab extends DataDisplayTab
{
	private final int OFFSET = 5;
	private final int NUM_OF_GENENERATED_COLUMNS = 13;
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
		addMenuAndMenuItems(SufuFactory.newButton("Add table", evt -> addTable()));
		addContentPanel();
		buildTableHeader();
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
		SufuTableTools.addColumnHeader(contentPanel, baseX++, baseY, String.valueOf(yearOverview.year()), "Income", "Spendings", "Outcome");
		int xPos = 0;
		int incomeY = baseY + 1;
		int spendigsY = incomeY + 1;
		int outcomeY = spendigsY + 1;
		for (int x = baseX; x < NUM_OF_GENENERATED_COLUMNS; x++)
		{
			contentPanel.add(SufuFactory.newTextFieldHeader(income[xPos].toString()), contentPanel.getGBC(x, incomeY));
			contentPanel.add(SufuFactory.newTextFieldHeader(spendings[xPos].toString()), contentPanel.getGBC(x, spendigsY));
			contentPanel.add(SufuFactory.newTextFieldHeader((income[xPos].add(spendings[xPos]).toString())), contentPanel.getGBC(x, outcomeY)); // outcome is already negative
			xPos++;
		}
		SufuTableTools.addColumnHeader(contentPanel, NUM_OF_GENENERATED_COLUMNS, ++baseY, yearOverview.totalIncome(), yearOverview.totalSpendings(), yearOverview.totalResult());
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
