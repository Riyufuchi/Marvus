package riyufuchi.marvus.marvusLib.financialRecords;

public record DataSummary(int transactionsTotal, double totalIncome, double totalSpendigs, double totalOutcome, double avgIncome, double avgSpendings, 
		double avgOutcome, double[] avgTransactionsPerMonth, double avgTransactionPerYear)
{

}
