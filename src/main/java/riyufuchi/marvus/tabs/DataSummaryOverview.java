package riyufuchi.marvus.tabs;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvusLib.records.DataSummary;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class DataSummaryOverview extends DataDisplayMode
{
	private final String[] captions = {"Transactions", "Income", "Spendings", "Outcome"};
	private final String format = "%.2f";
	private int xIndex, yIndex;
	private DataSummary ds;
	
	public DataSummaryOverview(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		resetValues();
		this.ds = dataSource.getDataSummary(MarvusConfig.financialYear);
	}
	
	@Override
	public void prepareUI()
	{
		yIndex = 1;
		for (String s : captions)
			masterPanel.add(SufuFactory.newTextFieldHeader(s), targetWindow.getGBC(0, yIndex++));
		masterPanel.add(SufuFactory.newTextFieldHeader(String.valueOf(MarvusConfig.financialYear)), targetWindow.getGBC(0, 0));
		masterPanel.add(SufuFactory.newTextFieldHeader("Total"), targetWindow.getGBC(1, 0));
		masterPanel.add(SufuFactory.newTextFieldHeader("Average per year"), targetWindow.getGBC(2, 0));
		resetValues();
	}

	@Override
	public void displayData()
	{
		// total values
		addInfoItem(String.valueOf(ds.transactionsTotal()));
		addInfoItem(String.valueOf(ds.totalIncome()));
		addInfoItem(String.valueOf(ds.totalSpendigs()));
		addInfoItem(String.valueOf(ds.totalOutcome()));
		// avg. values
		newColumn();
		addInfoItem(String.format(format, ds.avgTransactionPerYear()));
		addInfoItem(String.format(format, ds.avgIncome()));
		addInfoItem(String.format(format, ds.avgSpendings()));
		addInfoItem(String.format(format, ds.avgOutcome()));
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 6);
		resetValues();
		ds = dataSource.getDataSummary(MarvusConfig.financialYear);
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
		masterPanel.add(SufuFactory.newTextFieldHeader(info), targetWindow.getGBC(xIndex, yIndex));
	}
}
