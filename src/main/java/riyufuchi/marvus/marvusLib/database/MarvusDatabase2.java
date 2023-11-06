package riyufuchi.marvus.marvusLib.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataStorage.MarvusDataTable;
import riyufuchi.marvus.marvusLib.dataStorage.MarvusDataTableCategorized;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.interfaces.IDatabase;
import riyufuchi.marvus.marvusLib.records.DataSummary;
import riyufuchi.marvus.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * This class doesn't represent actual connection to database, just "simulates" it
 * 
 * @author Riyufuchi
 * @version 1.1 - 09.10.2023
 * @since 1.94 - 07.10.2023
 */
public class MarvusDatabase2 extends MarvusDataTableCategorized implements IDatabase<Transaction>
{
	public static MaruvsDatabaseUtils utils = new MaruvsDatabaseUtils();
	private transient Consumer<String> errorHandler;
	private transient Comparator<FinancialCategory> sorter;
	
	public MarvusDatabase2()
	{
		super();
		this.errorHandler = e -> System.out.println(e);
		this.sorter = TransactionComparation.compareFC(CompareMethod.By_name);
	}
	
	public MarvusDatabase2(Consumer<String> errorHandler)
	{
		super();
		this.sorter = TransactionComparation.compareFC(CompareMethod.By_name);
		if (errorHandler == null)
			this.errorHandler = e -> System.out.println(e);
		else
			this.errorHandler = errorHandler;
	}
	
	public void sort()
	{
		for (int i = 0; i < 11; i++)
			Collections.sort(getCategorizedMonth(i), sorter);
	}
	
	/**
	 * Set new comparator and sort by it
	 * 
	 * @param comp
	 */
	public void setComparator(Comparator<FinancialCategory> comp)
	{
		if (comp == null)
			return;
		sorter = comp;
		for (int i = 0; i < 11; i++)
			Collections.sort(getCategorizedMonth(i), comp);
	}
	
	public void setErrorHandler(Consumer<String> errorHandler)
	{
		if (errorHandler == null)
			return;
		this.errorHandler = errorHandler;
	}
	
	// GETTERS
	
	public LinkedList<FinancialCategory> getCategorizedYearByCategories(int year)
	{
		LinkedList<FinancialCategory> list = new LinkedList<>();
		FinancialCategory holder = null;
		for (Transaction t : this)
		{
			if (t.getDate().getYear() != year)
				continue;
			holder = new FinancialCategory(t.getCategory(), t);
			for (FinancialCategory mc : list)
			{
				if (mc.getCategory().equals(holder.getCategory()))
				{
					mc.add(t);
					holder = null;
					break;
				}
			}
			if(holder != null)
				list.add(holder);
		}
		return list;
	}
	
	public YearOverview getYearOverview(int year)
	{
		BigDecimal[] income = new BigDecimal[12];
		BigDecimal[] spendings = new BigDecimal[12];
		BigDecimal zero = new BigDecimal(0);
		int index = 0;
		for (int i = 0; i < 12; i++)
		{
			income[i] = new BigDecimal(0);
			spendings[i] = new BigDecimal(0);
		}
		for (Transaction t : this)
		{
			if (t.getDate().getYear() == year)
			{
				index = t.getDate().getMonthValue() - 1;
				switch (t.getValue().compareTo(zero))
				{
					case 1 -> income[index] = income[index].add(t.getValue());
					case -1 -> spendings[index] = spendings[index].add(t.getValue());
					case 0 -> errorHandler.accept("Zero value detected for:\n" + t.getID() + " -> " + t.toString());
				}
			}
		}
		BigDecimal totalOutcome = new BigDecimal(0);
		for (int i = 0; i < 12; i++)
		{
			zero = zero.add(income[i]);
			totalOutcome = totalOutcome.add(spendings[i]);
		}
		return new YearOverview(year, income, spendings, zero, totalOutcome, zero.add(totalOutcome));
	}
	
	public DataSummary getDataSummary(int year)
	{
		YearOverview yo = getYearOverview(year);
		BigDecimal twelve = new BigDecimal(12);
		
		double avgIncome = yo.totalIncome().divide(twelve, 2, RoundingMode.HALF_UP).doubleValue();
		double avgSpendings = yo.totalOutcome().divide(twelve, 2, RoundingMode.HALF_UP).doubleValue();
		double avgTotal = yo.totalResult().divide(twelve, 2, RoundingMode.HALF_UP).doubleValue();
		
		double[] avgTransactionsPerMonth = new double[12];
		double avg = 0;
		
		Month[] monthsArr = Month.values();
		boolean leapYear = SufuDateUtils.isLeapYear(year); 
		int numOfDays = 0;
		
		for(int i = 0; i < 11; i++)
		{
			numOfDays = monthsArr[i].length(leapYear);
			for(FinancialCategory fc : getCategorizedMonth(i))
			{
				avg += fc.size();
			}
			avgTransactionsPerMonth[i] = avg / numOfDays;
		}
		
		if (leapYear)
			numOfDays = 365;
		else
			numOfDays = 366;
		
		return new DataSummary(size(), yo.totalIncome().doubleValue(), yo.totalOutcome().doubleValue(), yo.totalResult().doubleValue(),
				avgIncome, avgSpendings, avgTotal, avgTransactionsPerMonth, size() / (double)numOfDays);
	}
	
	public LinkedList<FinancialCategory> getCategorizedMonthByCategory(Month month)
	{
		if (month == null)
			return new LinkedList<>();
		return  getCategorizedMonthByCategory(month.getValue());
	}
	
	private LinkedList<FinancialCategory> getCategorizedMonthByCategory(int monthOrderNum)
	{
		LinkedList<FinancialCategory> list = new LinkedList<>();
		FinancialCategory holder = null;
		for (Transaction t : this)
		{
			if (t.getDate().getMonthValue() == monthOrderNum)
			{
				holder = new FinancialCategory(t.getCategory(), t);
				for (FinancialCategory mc : list)
				{
					if (mc.getCategory().equals(holder.getCategory()))
					{
						mc.add(t);
						holder = null;
						break;
					}
				}
				if(holder != null)
					list.add(holder);
			}
		}
		Collections.sort(list, sorter);
		return list;
	}

	@Override
	public Optional<Transaction> getByID(final int ID)
	{
		for (int i = 0; i < 11; i++)
		{
			for (FinancialCategory fc : getCategorizedMonth(i))
			{
				for (Transaction t : fc)
				{
					if (t.getID() == ID)
						return Optional.of(t);
				}
			}
		}
		return Optional.empty();
	}

}
