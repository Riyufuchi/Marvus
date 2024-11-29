package riyufuchi.marvusLib.database;

import java.math.BigDecimal;
import java.math.RoundingMode;

import riyufuchi.marvusLib.interfaces.MarvusQuerriable;
import riyufuchi.marvusLib.records.MarvusDataSummary;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;;

/**
 * @author riyufuchi
 * @since 09.09.2024
 * @version 29.11.2024
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
			case "Name" -> database.stream().forEach(e -> {
				if (e.getName().equals(oldValue))
					e.setName(newValue);
			});
		}
		return true;
	}
	
	public void updateNameWhenNameValue(String newName, String name, String value)
	{
		BigDecimal valueNum = new BigDecimal(value);
		database.stream().forEach(e -> {
			if (e.getName().equals(name) && e.getValue().compareTo(valueNum) <= 0)
				e.setName(newName);
		});
	}

	@Override
	public boolean updateItemWhere(String whereAttr, String whereValue, String targetAttr, String newValue)
	{
		if (whereAttr == null || whereValue == null || newValue == null)
			return false;
		switch (whereAttr)
		{
			case "Value" -> updateNameWhenValue(new BigDecimal(whereValue), newValue);
			case "Name" -> database.stream().filter(t -> t.getName().equals(whereValue)).forEach(t -> t.setName(newValue));
			default -> { return false; }
		}
		return true;
	}
	
	private void updateNameWhenValue(BigDecimal value, String name)
	{
		database.stream().forEach(e -> {
			if (e.getValue().compareTo(value) == 0)
				e.setName(name);
		});
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
