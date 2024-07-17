package riyufuchi.marvusLib.dataDisplay;

import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvusLib.records.DataSummary;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class DataSummaryOverview extends DataDisplayMode
{
	private final String[] captions = {"Total transactions", " Total income", "Total spendings", "Total outcome"};
	private final String perYear = "per Year";
	
	public DataSummaryOverview(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		int y = 0;
		for (String s : captions)
		{
			panel.add(SufuFactory.newTextFieldHeader(s), targetWindow.getGBC(0, y));
			panel.add(SufuFactory.newTextFieldHeader(perYear), targetWindow.getGBC(2, y++));
		}
		DataSummary ds = dataSource.getDataSummary(MarvusConfig.financialYear);
		panel.add(SufuFactory.newTextFieldHeader(String.format("%d (avg: %.2f)", ds.transactionsTotal(), ds.avgTransactionPerYear())), targetWindow.getGBC(1, 0));
		panel.add(SufuFactory.newTextFieldHeader(String.format("%.2f (avg: %.2f)", ds.totalIncome(), ds.avgIncome())), targetWindow.getGBC(1, 1));
		panel.add(SufuFactory.newTextFieldHeader(String.format("%.2f (avg: %.2f)", ds.totalSpendigs(), ds.avgSpendings())), targetWindow.getGBC(1, 2));
		panel.add(SufuFactory.newTextFieldHeader(String.format("%.2f (avg: %.2f)", ds.totalOutcome(), ds.avgOutcome())), targetWindow.getGBC(1, 3));
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
