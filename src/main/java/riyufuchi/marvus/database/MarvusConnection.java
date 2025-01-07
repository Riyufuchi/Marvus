package riyufuchi.marvus.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusQuerriable;
import riyufuchi.marvusLib.records.MarvusDataStatistics;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.time.SufuDateUtils;;

/**
 * @author riyufuchi
 * @since 09.09.2024
 * @version 01.01.2025
 */
public class MarvusConnection implements MarvusQuerriable
{
	private MarvusDatabase database;

	public MarvusConnection(MarvusDatabase databse)
	{
		this.database = databse;
	}

	@Override
	public boolean updateAtribbute(String atr, String oldValue, String newValue)
	{
		switch (atr)
		{
			case "Name" -> database.stream().filter(t -> t.getName().equals(oldValue)).forEach(t -> t.setName(newValue));
			case "Category" -> database.stream().filter(t -> t.getCategory().equals(oldValue)).forEach(t -> t.setCategory(newValue));
			default -> { updateAll(createStreamFor(atr, oldValue), createPredicateFor("", ""), createConsumerFor(atr, newValue)); }
		}
		return true;
	}
	
	@Override
	public boolean updateAtribbute(String whereAttr, String whereValue, String targetAttr, String oldValue, String newValue)
	{
		if (whereAttr == null || whereValue == null || targetAttr == null || newValue == null || oldValue == null)
			return false;
		updateAll(createStreamFor(whereAttr, whereValue), createPredicateFor(targetAttr, oldValue), createConsumerFor(targetAttr, newValue));
		return true;
	}
	
	private Stream<Transaction> createStreamFor(String targetAttr, String whereValue)
	{
		return database.stream().filter(createPredicateFor(targetAttr, whereValue));
	}
	
	private Predicate<Transaction> createPredicateFor(String targetAttr, String whereValue)
	{
		if (whereValue.isBlank())
			return t -> true;
		switch (targetAttr)
		{
			case "Name" -> { return t -> t.getName().equals(whereValue); }
			case "Category" -> { return t -> t.getCategory().equals(whereValue); }
			default -> { return t -> false; }
		}
	}
	
	private Consumer<Transaction> createConsumerFor(String targetAttr, String newValue)
	{
		switch (targetAttr)
		{
			case "" -> { return t -> {}; }
			case "Name" -> { return t -> t.setName(newValue); }
			case "Category" -> { return t -> t.setCategory(newValue); }
			default -> { return t -> {}; }
		}
	}
	
	private void updateAll(Stream<Transaction> stream, Predicate<Transaction> predicate, Consumer<Transaction> consumer)
	{
		stream.filter(predicate).forEach(consumer);
	}
	
	@Override
	public MarvusDataStatistics createDataStatistics(int year)
	{
		final BigDecimal TOTAL_TRANSACTION = new BigDecimal(database.size());
		final BigDecimal TWELVE = new BigDecimal(12);
		BigDecimal numOfDays = new BigDecimal(365);
		if (SufuDateUtils.isLeapYear(year))
			numOfDays.add(BigDecimal.ONE);
		// Average transactions
		BigDecimal avgYearTransactions = TOTAL_TRANSACTION.divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgMonthTransactions = TOTAL_TRANSACTION.divide(numOfDays, 2, RoundingMode.HALF_UP);
		// Average income, outcome and total
		YearOverview yo = database.getYearOverview(year);
		BigDecimal avgIncome = yo.totalIncome().divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgSpendings = yo.totalSpendings().divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgTotal = yo.totalResult().divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgDailyIncome = yo.totalIncome().divide(numOfDays, 2, RoundingMode.HALF_UP);
		BigDecimal avgDailySpendings = yo.totalSpendings().divide(numOfDays, 2, RoundingMode.HALF_UP);
		BigDecimal avgDailyTotal = yo.totalResult().divide(numOfDays, 2, RoundingMode.HALF_UP);
		
		return new MarvusDataStatistics(TOTAL_TRANSACTION.intValue(), avgYearTransactions, avgMonthTransactions,
				yo.totalIncome(), yo.totalSpendings(), yo.totalResult(),
				avgIncome, avgSpendings, avgTotal,
				avgDailyIncome, avgDailySpendings, avgDailyTotal);
	}
}
