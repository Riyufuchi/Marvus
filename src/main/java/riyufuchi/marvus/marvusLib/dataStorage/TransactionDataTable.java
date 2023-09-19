package riyufuchi.marvus.marvusLib.dataStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.financialRecords.DataSummary;
import riyufuchi.marvus.marvusLib.financialRecords.YearOverview;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * This class sort data into categories. Data starts from x = 0.
 * 
 * Created On: 24.08.2023<br>
 * Last Edit: 18.09.2023
 * 
 * @author Riyufuchi
 * @version 1.7
 * @since 0.1.60
 */
public class TransactionDataTable
{
	private ArrayList<LinkedList<FinancialCategory>> months;
	private DataBox<Transaction> dataBox;
	private MarvusDataWindow bdt;
	private int x, size;
	
	public TransactionDataTable(MarvusDataWindow bdt)
	{
		initialize();
		this.x = 0;
		//if (bdt == null)
			//bdt = new BudgetDataTable();
		this.bdt = bdt;
		this.dataBox = new DataBox<>(e -> SufuDialogHelper.exceptionDialog(bdt, e),
				TransactionComparation.compareBy(CompareMethod.OldestToNewest));
	}
	
	private void initialize()
	{
		this.months = new ArrayList<>();
		for (int i = 0; i < 12; i++)
			months.add(new LinkedList<>());
		this.size = 0;
	}
	
	// Data structure methods
	
	public void add(Transaction transaction)
	{
		if (transaction == null)
			return;
		dataBox.add(transaction);
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
		{
			months.get(x).add(new FinancialCategory(transaction));
			size++;
			return;
		}
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				data.add(transaction);
				x = 0;
				size++;
				return;
			}
		});
		if (x != 0)
		{
			months.get(x).add(new FinancialCategory(transaction));
			size++;
		}
	}
	
	public void set(Transaction transaction)
	{
		if (transaction == null)
			return;
		x = transaction.getDate().getMonthValue() - 1;
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				data.forEach(transactionOld -> {
					if (transactionOld.getID() == transaction.getID())
					{
						transactionOld = transaction;
						return;
					}
				});
				return;
			}
		});
	}
	
	public void remove(int month, String name, int id)
	{
		if(name == null || name.isBlank())
			return;
		x = month - 1;
		if (months.get(x).isEmpty())
			return;
		months.get(x).stream().forEach(data -> {
			if (data.getCategory().equals(name))
			{
				data.forEach(t -> {
					if (t.getID() == id)
					{
						dataBox.getList().remove(t);
						data.remove(t);
						size--;
						return;
					}
				});
				return;
			}
		});
	}
	
	public void remove(Transaction transaction)
	{
		if (transaction == null)
			return;
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
			return;
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				dataBox.getList().remove(transaction);
				data.remove(transaction);
				return;
			}
		});
		size--;
	}
	
	public FinancialCategory get(int x, int y)
	{
		return months.get(x).get(y);
	}
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	public void clear()
	{
		initialize();
	}
	
	public int size()
	{
		return size;
	}
	
	public void rebuild()
	{
		clear();
		for (int i = 1; i < 13; i++)
			months.set(i - 1, TransactionCalculations.categorizeMonth(dataBox, i));
		size = dataBox.getList().size();
	}
	
	// Money operations
	
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
		for (Transaction t : dataBox)
		{
			if (t.getDate().getYear() == year)
			{
				index = t.getDate().getMonthValue() - 1;
				switch (t.getValue().compareTo(zero))
				{
					case 1 -> income[index] = income[index].add(t.getValue());
					case -1 -> spendings[index] = spendings[index].add(t.getValue());
					case 0 -> SufuDialogHelper.warningDialog(bdt, "Zero value detected for: "
					+ t.toString(), "Zero money in transaction " + t.getID());
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
			for(FinancialCategory fc : months.get(i))
			{
				avg += fc.size();
			}
			avgTransactionsPerMonth[i] = avg / numOfDays;
		}
		
		return new DataSummary(size, yo.totalIncome().doubleValue(), yo.totalOutcome().doubleValue(), yo.totalResult().doubleValue(),
				avgIncome, avgSpendings, avgTotal, avgTransactionsPerMonth, size / 365.0);
	}

	// Getters
	
	/**
	 * 
	 * @param index month number - 1
	 * @return
	 */
	public LinkedList<FinancialCategory> getCategorizedMonth(int index)
	{
		return months.get(index);
	}
	
	public LinkedList<FinancialCategory> getCategorizedMonthByCategory(int index)
	{
		return TransactionCalculations.categorizeMonthByCategories(dataBox, index + 1);
	}
	
	public DataBox<Transaction> getDataBox()
	{
		return dataBox;
	}
}
