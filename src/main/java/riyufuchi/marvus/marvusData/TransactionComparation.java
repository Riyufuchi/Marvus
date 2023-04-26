package riyufuchi.marvus.marvusData;

import java.util.Comparator;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 27.04.2023
 * 
 * @author Riyufuchi
 */
public class TransactionComparation
{
	public enum CompareMethod
	{
		OldestToNewest,
		NewestToOldest,
		Up,
		Down,
		ByName,
		ID;
	};
	
	private TransactionComparation()
	{}
	
	//TODO: Finish implementation of the comparators
	public static Comparator<Transaction> compareBy(CompareMethod compareMethod)
	{
		if(compareMethod == null)
			return (m1, m2) -> compareID(m1, m2);
		switch (compareMethod)
		{
			case ByName -> { return (m1, m2) -> { return m1.getName().compareTo(m2.getName()); };}
			case Down -> { }
			case NewestToOldest -> { return (m1, m2) -> { return m2.getDate().compareTo(m1.getDate()); };}
			case OldestToNewest -> { return (m1, m2) -> { return m1.getDate().compareTo(m2.getDate()); };}
			case Up -> { }
			case ID -> { return (m1, m2) -> compareID(m1, m2); }
		}
		return null;
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
