package riyufuchi.marvus.marvusLib.dataStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.financialRecords.DataSummary;
import riyufuchi.marvus.marvusLib.financialRecords.YearOverview;
import riyufuchi.marvus.marvusLib.interfaces.MarvusCollection;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * This class sort data into categories. Data starts from x = 0
 * 
 * @author Riyufuchi
 * @version 2.0 - 04.10.2023
 * @since 0.1.60 - 24.08.2023
 */
public class TransactionDataTable implements MarvusCollection<Transaction>
{
	private ArrayList<LinkedList<FinancialCategory>> months;
	//private DataBox<Transaction> dataBox;
	private MarvusDataFrame mdt;
	private int x, size;
	
	public TransactionDataTable(MarvusDataFrame mdt)
	{
		initialize();
		this.x = 0;
		this.mdt = mdt;
		/*this.dataBox = new DataBox<>(e -> SufuDialogHelper.exceptionDialog(mdt.getSelf(), e),
				TransactionComparation.compareBy(CompareMethod.OldestToNewest));*/
	}
	
	private void initialize()
	{
		this.months = new ArrayList<>();
		for (int i = 0; i < 12; i++)
			months.add(new LinkedList<>());
		this.size = 0;
	}
	
	// Java Collection
	
	@Override
	public boolean add(Transaction transaction)
	{
		if (transaction == null)
			return false;
		//dataBox.add(transaction);
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
		{
			months.get(x).add(new FinancialCategory(transaction));
			size++;
			return true;
		}
		months.get(x).stream().forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				data.add(transaction);
				x = -1;
				size++;
				return;
			}
		});
		if (x != -1)
		{
			months.get(x).add(new FinancialCategory(transaction));
			size++;
			return true;
		}
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends Transaction> c)
	{
		c.stream().forEach(item -> add(item));
		return true;
	}
	
	@Override
	public boolean remove(Object o)
	{
		if (o instanceof Transaction)
			return remove((Transaction)o);
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		int preRemoveSize = size;
		c.stream().forEach(item -> remove(item));
		return size < preRemoveSize;
	}
	
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	@Override
	public void clear()
	{
		initialize();
	}
	
	@Override
	public int size()
	{
		return size;
	}
	
	@Override
	public boolean contains(Object o)
	{
		if (o == null)
			return false;
		return stream().anyMatch(pre -> pre.equals(o));
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		if (c == null || c.isEmpty())
			return false;
		int num = c.size();
		Iterator<?> it = c.iterator();
		for (Transaction t : this)
		{
			while (it.hasNext())
			{
				if (t.equals(it.next()))
				{
					try
					{
						it.remove();
					}
					catch (Exception e)
					{
						c.remove(t);
					}
					num--;
				}
			}
		}
		return num == 0;
	}
	
	@Override
	public Iterator<Transaction> iterator()
	{
		return toList().iterator();
	}
	
	// Marvus Collection
	
	@Override
	public LinkedList<Transaction> toList()
	{
		LinkedList<Transaction> data = new LinkedList<>();
		for (int i = 0; i < 11; i++)
		{
			for (FinancialCategory fc : months.get(i))
			{
				data.addAll(fc);
			}
		}
		return data;
	}
	
	// Specific methods
	
	public void rebuild()
	{
		Iterator<Transaction> it = iterator();
		clear();
		while (it.hasNext())
		{
			add(it.next());
		}
		/*
		for (int i = 1; i < 13; i++)
		{
			months.set(i - 1, TransactionCalculations.categorizeMonth(this, i));
		}
		//size = dataBox.getList().size();*/
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
						//dataBox.getList().remove(t);
						data.remove(t);
						size--;
						return;
					}
				});
				return;
			}
		});
	}
	
	public boolean remove(Transaction transaction)
	{
		if (transaction == null)
			return false;
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
			return false;
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				//dataBox.getList().remove(transaction);
				data.remove(transaction);
				return;
			}
		});
		size--;
		return true;
	}
	
	public FinancialCategory get(int x, int y)
	{
		return months.get(x).get(y);
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
		for (Transaction t : this)
		{
			if (t.getDate().getYear() == year)
			{
				index = t.getDate().getMonthValue() - 1;
				switch (t.getValue().compareTo(zero))
				{
					case 1 -> income[index] = income[index].add(t.getValue());
					case -1 -> spendings[index] = spendings[index].add(t.getValue());
					case 0 -> SufuDialogHelper.warningDialog(mdt.getSelf(), "Zero value detected for: "
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
		
		if (leapYear)
			numOfDays = 365;
		else
			numOfDays = 366;
		
		return new DataSummary(size, yo.totalIncome().doubleValue(), yo.totalOutcome().doubleValue(), yo.totalResult().doubleValue(),
				avgIncome, avgSpendings, avgTotal, avgTransactionsPerMonth, size / (double)numOfDays);
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
		return TransactionCalculations.categorizeMonthByCategories(this, index + 1);
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	public DataBox<Transaction> getDataBox()
	{
		return dataBox;
	}*/
}
