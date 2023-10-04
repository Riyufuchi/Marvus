package riyufuchi.marvus.marvusLib.dataUtils;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 04.10.2023
 * @since 18.04.2023
 */
public class TransactionCalculations
{
	private TransactionCalculations()
	{
	}
	
	/**
	 * Calculates incomes and outcomes in specified month and shows result -> income - outcome 
	 * 
	 * @param month
	 * @return
	 */
	public static Consumer<Iterable<Transaction>> incomeToSpendings(JFrame parentFrame, Month month)
	{
		return data -> {
			if (month == null)
				return;
			BigDecimal spendings = new BigDecimal(0);
			BigDecimal income = new BigDecimal(0);
			BigDecimal zero = new BigDecimal(0);
			int monthNum = month.getValue();
			for(Transaction t : data)
			{
				if (t.getDate().getMonthValue() == monthNum)
				{
					switch (t.getValue().compareTo(zero))
					{
						case 1 -> income = income.add(t.getValue());
						case -1 -> spendings = spendings.add(t.getValue());
						case 0 -> SufuDialogHelper.warningDialog(parentFrame, "Zero value detected for: " + t.toString() + "\nSome data can be missing", "Zero money value");
					}
				}
			}
			SufuDialogHelper.informationDialog(parentFrame, (income + " " + spendings + " = "  + income.add(spendings)), "Ratio in " + month.toString());
		};
	}

	public static LinkedList<FinancialCategory> categorizeMonth(Collection<Transaction> data, int month)
	{
		LinkedList<FinancialCategory> list = new LinkedList<>();
		FinancialCategory holder = null;
		for (Transaction t : data)
		{
			if (t.getDate().getMonthValue() == month)
			{
				holder = new FinancialCategory(t);
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
		return list;
	}
	
	public static LinkedList<FinancialCategory> categorizeMonthByCategories(Iterable<Transaction> data, int month)
	{
		LinkedList<FinancialCategory> list = new LinkedList<>();
		FinancialCategory holder = null;
		for (Transaction t : data)
		{
			if (t.getDate().getMonthValue() == month)
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
		return list;
	}
	
	public static LinkedList<FinancialCategory> categorizeYearByCategories(Collection<Transaction> data)
	{
		LinkedList<FinancialCategory> list = new LinkedList<>();
		FinancialCategory holder = null;
		for (Transaction t : data)
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
		return list;
	}
}
