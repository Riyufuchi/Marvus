package riyufuchi.marvus.marvusLib.records;

public record DataSummary(int transactionsTotal, double totalIncome, double totalSpendigs, double totalOutcome, double avgIncome, double avgSpendings, 
		double avgOutcome, double[] avgTransactionsPerMonth, double avgTransactionPerYear)
{

}
