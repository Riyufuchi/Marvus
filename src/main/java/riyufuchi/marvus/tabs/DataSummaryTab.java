package riyufuchi.marvus.tabs;

import riyufuchi.marvus.database.MarvusConnection;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.interfaces.MarvusQuerriable;
import riyufuchi.marvusLib.records.MarvusDataStatistics;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

/**
 * @author riyufuchi
 * @since ?
 * @version 01.01.2025
 */
public class DataSummaryTab extends DataDisplayTab
{
	private final String[] CAPTIONS;
	private final String FORMAT;
	private final int YEAR;
	private int xIndex, yIndex;
	private MarvusQuerriable con;
	private MarvusDataStatistics ds;
	
	public DataSummaryTab(MarvusTabbedFrame targetWindow, int year)
	{
		super(targetWindow);
		this.CAPTIONS = new String[]{"Transactions", "Income", "Spendings", "Outcome"};
		this.FORMAT = "%.2f";
		this.YEAR = year;
		this.con = new MarvusConnection(dataSource);
		this.ds = con.createDataStatistics(YEAR);
		// UI
		resetValues();
		this.yIndex = 1;
		for (String s : CAPTIONS)
			masterPanel.add(SufuFactory.newTextFieldHeader(s), masterPanel.getGBC(0, yIndex++));
		masterPanel.add(SufuFactory.newTextFieldHeader(String.valueOf(YEAR)), masterPanel.getGBC(0, 0));
		masterPanel.add(SufuFactory.newTextFieldHeader("Year total"), masterPanel.getGBC(1, 0));
		masterPanel.add(SufuFactory.newTextFieldHeader("Year average"), masterPanel.getGBC(2, 0));
		masterPanel.add(SufuFactory.newTextFieldHeader("Daily average"), masterPanel.getGBC(3, 0));
		resetValues();
	}

	@Override
	public void displayData()
	{
		// Year total values
		addInfoItem(String.valueOf(ds.transactionsTotal()));
		addInfoItem(String.valueOf(ds.totalIncome()));
		addInfoItem(String.valueOf(ds.totalSpendigs()));
		addInfoItem(String.valueOf(ds.totalOutcome()));
		// Avg. year values
		newColumn();
		addInfoItem(String.format(FORMAT, ds.avgTransactionsPerYear()));
		addInfoItem(String.format(FORMAT, ds.avgIncome()));
		addInfoItem(String.format(FORMAT, ds.avgSpendings()));
		addInfoItem(String.format(FORMAT, ds.avgOutcome()));
		// Avg. daily values
		newColumn();
		addInfoItem(String.format(FORMAT, ds.avgTransactionsPerDay()));
		addInfoItem(String.format(FORMAT, ds.avdDailyIncome()));
		addInfoItem(String.format(FORMAT, ds.avgDailySpendings()));
		addInfoItem(String.format(FORMAT, ds.avgDailyOutcome()));
		resetValues();
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 7);
		resetValues();
		ds = con.createDataStatistics(YEAR);
		displayData();
	}
	
	private void resetValues()
	{
		yIndex = 0;
		xIndex = 1;
	}
	
	private void newColumn()
	{
		yIndex = 0;
		xIndex++;
	}
	
	private void addInfoItem(String info)
	{
		yIndex++;
		masterPanel.add(SufuFactory.newTextFieldHeader(info), masterPanel.getGBC(xIndex, yIndex));
	}
}
