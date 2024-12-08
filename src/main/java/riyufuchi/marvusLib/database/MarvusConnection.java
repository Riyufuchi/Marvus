package riyufuchi.marvusLib.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusQuerriable;
import riyufuchi.marvusLib.records.MarvusDataSummary;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;;

/**
 * @author riyufuchi
 * @since 09.09.2024
 * @version 08.12.2024
 */
public class MarvusConnection implements MarvusQuerriable
{
	private MarvusDatabase	database;

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
	public MarvusDataSummary createDataSummary(int year)
	{
		final BigDecimal TOTAL_TRANSACTION = new BigDecimal(database.size());
		final BigDecimal TWELVE = new BigDecimal(12);
		//final boolean LEAP_YEAR = SufuDateUtils.isLeapYear(year); 
		BigDecimal numOfDays = new BigDecimal(365);
		if (SufuDateUtils.isLeapYear(year))
			numOfDays.add(new BigDecimal(1));
		// Average transactions
		double avgYearTransactions = TOTAL_TRANSACTION.divide(TWELVE, 2, RoundingMode.HALF_UP).doubleValue();
		double avgMonthTransactions = TOTAL_TRANSACTION.divide(numOfDays, 2, RoundingMode.HALF_UP).doubleValue();
		// Average income, outcome and total
		YearOverview yo = database.getYearOverview(year);
		double avgIncome = yo.totalIncome().divide(TWELVE, 2, RoundingMode.HALF_UP).doubleValue();
		double avgSpendings = yo.totalSpendings().divide(TWELVE, 2, RoundingMode.HALF_UP).doubleValue();
		double avgTotal = yo.totalResult().divide(TWELVE, 2, RoundingMode.HALF_UP).doubleValue();
		double avgDailyIncome = yo.totalIncome().divide(numOfDays, 2, RoundingMode.HALF_UP).doubleValue();
		double avgDailySpendings = yo.totalSpendings().divide(numOfDays, 2, RoundingMode.HALF_UP).doubleValue();
		double avgDailyTotal = yo.totalResult().divide(numOfDays, 2, RoundingMode.HALF_UP).doubleValue();
		
		return new MarvusDataSummary(TOTAL_TRANSACTION.intValue(), avgYearTransactions, avgMonthTransactions,
				yo.totalIncome().doubleValue(), yo.totalSpendings().doubleValue(), yo.totalResult().doubleValue(),
				avgIncome, avgSpendings, avgTotal,
				avgDailyIncome, avgDailySpendings, avgDailyTotal);
	}
}
