package riyufuchi.marvus.tabs;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.database.MarvusConnection;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.interfaces.MarvusQuerriable;
import riyufuchi.marvusLib.records.MarvusDataSummary;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class DataSummaryTab extends DataDisplayTab
{
	private final String[] captions = {"Transactions", "Income", "Spendings", "Outcome"};
	private final String format = "%.2f";
	private int xIndex, yIndex;
	private MarvusQuerriable con;
	private MarvusDataSummary ds;
	
	public DataSummaryTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		resetValues();
		this.con = new MarvusConnection(dataSource);
		this.ds = con.createDataSummary(MarvusConfig.currentFinancialYear);
		// UI
		this.yIndex = 1;
		for (String s : captions)
			masterPanel.add(SufuFactory.newTextFieldHeader(s), masterPanel.getGBC(0, yIndex++));
		masterPanel.add(SufuFactory.newTextFieldHeader(String.valueOf(MarvusConfig.currentFinancialYear)), masterPanel.getGBC(0, 0));
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
		addInfoItem(String.format(format, ds.avgTransactionsPerYear()));
		addInfoItem(String.format(format, ds.avgIncome()));
		addInfoItem(String.format(format, ds.avgSpendings()));
		addInfoItem(String.format(format, ds.avgOutcome()));
		// Avg. daily values
		newColumn();
		addInfoItem(String.format(format, ds.avgTransactionsPerDay()));
		addInfoItem(String.format(format, ds.avdDailyIncome()));
		addInfoItem(String.format(format, ds.avgDailySpendings()));
		addInfoItem(String.format(format, ds.avgDailyOutcome()));
		resetValues();
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 7);
		resetValues();
		ds = con.createDataSummary(MarvusConfig.currentFinancialYear);
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
