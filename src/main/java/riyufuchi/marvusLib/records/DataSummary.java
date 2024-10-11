package riyufuchi.marvusLib.records;

@Deprecated
public record DataSummary(int transactionsTotal, double totalIncome, double totalSpendigs, double totalOutcome, double avgIncome, double avgSpendings, 
		double avgOutcome, double[] avgTransactionsPerMonth, double toatalAvgNumOfTransactionsPerMonth, double toatalAvgNumOfTransactionsPerYear)
{

}
