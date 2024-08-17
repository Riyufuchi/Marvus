package riyufuchi.marvus.tabs;

import java.awt.FlowLayout;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.util.LinkedList;

import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.lib.SufuLib;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGridPane;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author Riyufuchi
 * @since 1.66 - 05.09.2023
 * @version 17.08.2024
 */
public class YearOverviewTable extends DataDisplayMode
{
	private final int OFFSET = 5;
	private final int NUM_OF_GENENERATED_COLUMNS = 13;
	private JPanel menuPane;
	private SufuGridPane contentPane;
	private LinkedList<YearOverview> yearOverviews;
	private int yOffset;
	
	public YearOverviewTable(MarvusDataFrame targetWindow, int year)
	{
		super(targetWindow);
		this.yOffset = 0;
		this.yearOverviews = new LinkedList<>();
		yearOverviews.add(dataSource.getYearOverview(year));
	}
	
	// OVERRIDES
	
	@Override
	public void prepareUI()
	{
		menuPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		menuPane.add(SufuFactory.newButton("Add table", evt -> addTable()));
		
		contentPane = SufuFactory.newGridPane();
		
		masterPanel.add(menuPane, targetWindow.getGBC(0, 0));
		masterPanel.add(contentPane, targetWindow.getGBC(0, 1));
		buildTableHeader();
	}
	
	@Override
	public void displayData()
	{
		buildTable(yearOverviews.getFirst(), 0, 0);
	}
	
	@Override
	public void refresh()
	{
		yearOverviews.addFirst(dataSource.getYearOverview(yearOverviews.getFirst().year()));
		yearOverviews.remove(1);
		clearPanel(contentPane, NUM_OF_GENENERATED_COLUMNS);
		yOffset = 0;
		totalItems = 13;
		for (YearOverview yearOverview : yearOverviews)
		{
			buildTable(yearOverview, 0, yOffset);
			yOffset += OFFSET;
		}
	}
	
	// TODO: Allow adding more tables and operations with them
	private void addTable()
	{
		SufuLib.functionalityNotYetImplementedDialog(targetWindow.getSelf());
		String path = MarvusConfig.currentWorkFile.getAbsolutePath().replace("2024", "2023");
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
		SufuTableTools.addRowHeader(contentPane, 1, 0, Month.values());
		SufuTableTools.addColumnHeader(contentPane, NUM_OF_GENENERATED_COLUMNS, 0, "YEAR TOTAL");
		totalItems += 13;
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
		SufuTableTools.addColumnHeader(contentPane, baseX++, baseY, String.valueOf(yearOverview.year()), "Income", "Spendings", "Outcome");
		int xPos = 0;
		int incomeY = baseY + 1;
		int spendigsY = incomeY + 1;
		int outcomeY = spendigsY + 1;
		for (int x = baseX; x < NUM_OF_GENENERATED_COLUMNS; x++)
		{
			contentPane.add(SufuFactory.newTextFieldHeader(income[xPos].toString()), contentPane.getGBC(x, incomeY));
			contentPane.add(SufuFactory.newTextFieldHeader(spendings[xPos].toString()), contentPane.getGBC(x, spendigsY));
			contentPane.add(SufuFactory.newTextFieldHeader((income[xPos].add(spendings[xPos]).toString())), contentPane.getGBC(x, outcomeY)); // outcome is already negative
			xPos++;
		}
		SufuTableTools.addColumnHeader(contentPane, NUM_OF_GENENERATED_COLUMNS, ++baseY, yearOverview.totalIncome(), yearOverview.totalOutcome(), yearOverview.totalResult());
		totalItems += 42;
	}
}
