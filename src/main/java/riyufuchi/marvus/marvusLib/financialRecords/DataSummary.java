package riyufuchi.marvus.marvusLib.financialRecords;

public record DataSummary(int transactionsTotal, double totalIncome, double totalSpendigs, double totalRatio, double avgIncome, double avgSpendings, 
		double avgTotal, double[] avgTransactionsPerMonth, double avgTransactionPerYear)
{

}
