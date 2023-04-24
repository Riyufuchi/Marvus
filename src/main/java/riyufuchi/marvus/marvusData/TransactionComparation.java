package riyufuchi.marvus.marvusData;

import java.util.Comparator;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 24.04.2023
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
		ByName;
	};
	
	private TransactionComparation()
	{}
	
	public static Comparator<Transaction> compareBy(CompareMethod compareMethod)
	{
		switch (compareMethod)
		{
			case ByName -> { }
			case Down -> { }
			case NewestToOldest -> { }
			case OldestToNewest -> { return (m1, m2) -> { return m1.getDate().compareTo(m2.getDate()); };}
			case Up -> { }
		}
		return null;
	}
	
	public static Comparator<Transaction> byDateUpwards()
	{
		return (m1, m2) -> { return m1.getDate().compareTo(m2.getDate()); };
	}
}
