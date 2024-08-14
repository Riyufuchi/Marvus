package riyufuchi.marvus.tabs;

import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvusLib.records.DataSummary;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class DataSummaryOverview extends DataDisplayMode
{
	private final String[] captions = {"Transactions", "Income", "Spendings", "Outcome"};
	private final String format = "%.2f";
	
	public DataSummaryOverview(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		int y = 1;
		for (String s : captions)
			panel.add(SufuFactory.newTextFieldHeader(s), targetWindow.getGBC(0, y++));
		panel.add(SufuFactory.newTextFieldHeader("Total"), targetWindow.getGBC(1, 0));
		panel.add(SufuFactory.newTextFieldHeader("Average per year"), targetWindow.getGBC(2, 0));
		panel.add(SufuFactory.newTextFieldHeader(String.valueOf(MarvusConfig.financialYear)), targetWindow.getGBC(0, 0));
		DataSummary ds = dataSource.getDataSummary(MarvusConfig.financialYear);
		// total values
		panel.add(SufuFactory.newTextFieldHeader(String.valueOf(ds.transactionsTotal())), targetWindow.getGBC(1, 1));
		panel.add(SufuFactory.newTextFieldHeader(String.valueOf(ds.totalIncome())), targetWindow.getGBC(1, 2));
		panel.add(SufuFactory.newTextFieldHeader(String.valueOf(ds.totalSpendigs())), targetWindow.getGBC(1, 3));
		panel.add(SufuFactory.newTextFieldHeader(String.valueOf(ds.totalOutcome())), targetWindow.getGBC(1, 4));
		// avg. values
		panel.add(SufuFactory.newTextFieldHeader(String.format(format, ds.avgTransactionPerYear())), targetWindow.getGBC(2, 1));
		panel.add(SufuFactory.newTextFieldHeader(String.format(format, ds.avgIncome())), targetWindow.getGBC(2, 2));
		panel.add(SufuFactory.newTextFieldHeader(String.format(format, ds.avgSpendings())), targetWindow.getGBC(2, 3));
		panel.add(SufuFactory.newTextFieldHeader(String.format(format, ds.avgOutcome())), targetWindow.getGBC(2, 4));
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
