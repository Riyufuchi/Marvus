package riyufuchi.marvusLib.dataUtils;

import java.util.Comparator;

import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;

/**
 * @author Riyufuchi
 * @version 05.10.2023
 * @version 18.04.2023
 */
public class TransactionComparation
{
	/**
	 * This used in combobox for user to select from
	 */
	public enum CompareMethod
	{
		Oldest_to_newest,
		Newest_to_oldest,
		Lowest_to_highest,
		Highest_to_lowest,
		By_name,
		By_catagory,
		ID;
	};
	
	private TransactionComparation()
	{}
	
	public static Comparator<FinancialCategory> compareFC(CompareMethod compareMethod)
	{
		if(compareMethod == null)
			compareMethod = CompareMethod.By_name;
		switch (compareMethod)
		{
			case By_name -> { return (m1, m2) -> { return m1.getCategory().compareTo(m2.getCategory()); };}
			case Lowest_to_highest -> { return (m1, m2) -> { return m1.getSum().compareTo(m2.getSum()); };}
			case Highest_to_lowest -> { return (m1, m2) -> { return m2.getSum().compareTo(m1.getSum()); };}
			default -> { return (m1, m2) -> { return m1.getCategory().compareTo(m2.getCategory()); };}
		}
	}
	
	public static Comparator<Transaction> compareBy(CompareMethod compareMethod)
	{
		if(compareMethod == null)
			compareMethod = CompareMethod.Oldest_to_newest;
		switch (compareMethod)
		{
			case By_name -> { return (m1, m2) -> { return m1.getName().compareTo(m2.getName()); };}
			case By_catagory -> { return (m1, m2) -> { return m1.getCategory().compareTo(m2.getCategory()); };}
			case Lowest_to_highest -> { return (m1, m2) -> { return m1.getValue().compareTo(m2.getValue()); };}
			case Newest_to_oldest -> { return (m1, m2) -> { return m2.getDate().compareTo(m1.getDate()); };}
			case Oldest_to_newest -> { return (m1, m2) -> { return m1.getDate().compareTo(m2.getDate()); };}
			case Highest_to_lowest -> { return (m1, m2) -> { return m2.getValue().compareTo(m1.getValue()); };}
			case ID -> { return (m1, m2) -> compareID(m1, m2); }
			default -> { return (m1, m2) -> compareID(m1, m2); }
		}
	}
	
	private static int compareID(Transaction m1, Transaction m2)
	{
		if (m1.getID() > m2.getID())
			return 1;
		if (m1.getID() < m2.getID())
			return -1;
		return 0;
	}
}
