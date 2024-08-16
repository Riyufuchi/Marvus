package riyufuchi.marvus.tabs;

import java.awt.FlowLayout;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.util.Iterator;
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
 * @version 2.1 - 15.08.2024
 */
public class YearOverviewTable extends DataDisplayMode
{
	private final int OFFSET = 5;
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
	public void refresh()
	{
		//yearOverview = dataSource.getYearOverview(yearOverview.year());
		contentPane.removeAll();
		yOffset = 0;
		Iterator<YearOverview> it = yearOverviews.iterator();
		buildTable(it.next(), 0, yOffset, true);
		while (it.hasNext())
		{
			yOffset += OFFSET;
			buildTable(it.next(), 0, yOffset, false);
		}
	}

	@Override
	public void prepareUI()
	{
		menuPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		menuPane.add(SufuFactory.newButton("Add table", evt -> addTable()));
		
		contentPane = SufuFactory.newGridPane();
		
		masterPanel.add(menuPane, targetWindow.getGBC(0, 0));
		masterPanel.add(contentPane, targetWindow.getGBC(0, 1));
	}

	@Override
	public void displayData()
	{
		buildTable(yearOverviews.getFirst(), 0, 0, true);
	}
	
	// TODO: Allow adding more tables and operations with them
	private void addTable()
	{
		SufuLib.functionalityNotYetImplementedDialog(targetWindow.getSelf());
		String path = MarvusConfig.currentWorkFile.getAbsolutePath().replace("2024", "2023");
		System.out.print(path);
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
		/*if (db == null)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), "File not loaded", "Input error");
			return;
		}*/
		yearOverviews.add(db.getYearOverview(db.assumeYear()));
		targetWindow.refresh();
	}
	
	public void buildTable(YearOverview yearOverview, int baseX, int baseY, boolean addHeader)
	{
		if (yearOverview == null)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), "No input data!", "Data IO error");
			return;
		}
		BigDecimal[] income = yearOverview.income();
		BigDecimal[] spendings = yearOverview.spendigs();
		SufuTableTools.addColumnHeader(contentPane, baseX++, baseY, String.valueOf(yearOverview.year()), "Income", "Spendings", "Outcome");
		if (addHeader)
		{
			SufuTableTools.addRowHeader(contentPane, baseX, baseY, Month.values());
			SufuTableTools.addColumnHeader(contentPane, 13, baseY, "Year total");
		}
		int xPos = 0;
		int incomeY = baseY + 1;
		int spendigsY = incomeY + 1;
		int outcomeY = spendigsY + 1;
		for (int x = baseX; x < 13; x++)
		{
			contentPane.add(SufuFactory.newTextFieldHeader(income[xPos].toString()), contentPane.getGBC(x, incomeY));
			contentPane.add(SufuFactory.newTextFieldHeader(spendings[xPos].toString()), contentPane.getGBC(x, spendigsY));
			contentPane.add(SufuFactory.newTextFieldHeader((income[xPos].add(spendings[xPos]).toString())), contentPane.getGBC(x, outcomeY)); // outcome is already negative
			xPos++;
		}
		SufuTableTools.addColumnHeader(contentPane, 13, ++baseY, yearOverview.totalIncome(), yearOverview.totalOutcome(), yearOverview.totalResult());
	}
}
