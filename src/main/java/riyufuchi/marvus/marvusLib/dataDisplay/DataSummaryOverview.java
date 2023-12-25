package riyufuchi.marvus.marvusLib.dataDisplay;

import riyufuchi.marvus.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvus.marvusLib.records.DataSummary;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public class DataSummaryOverview extends DataDisplayMode
{
	public DataSummaryOverview(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
	}

	@Override
	public void displayData()
	{
		dataSummary();
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}

	private void dataSummary()
	{
		DataSummary ds = dataSource.getDataSummary(2023);
		String data = "Total transactions: %d (avg: %.2f)\n"
				+ "Total income: %.2f (avg: %.2f)\n"
				+ "Total spendings: %.2f (avg: %.2f)\n"
				+ "Total outcome: %.2f (avg: %.2f)\n";
		data = String.format(data, ds.transactionsTotal(), ds.avgTransactionPerYear(),
				ds.totalIncome(), ds.avgIncome(),
				ds.totalSpendigs(), ds.avgSpendings(),
				ds.totalOutcome(), ds.avgOutcome());
		SufuDialogHelper.informationDialog(targetWindow.getSelf(), data, "Data summary");
	}
}
