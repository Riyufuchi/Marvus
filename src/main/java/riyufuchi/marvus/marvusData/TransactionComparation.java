package riyufuchi.marvus.marvusData;

import java.util.Comparator;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 18.04.2023
 * 
 * @author Riyufuchi
 */
public class TransactionComparation {

	private TransactionComparation()
	{}
	
	public static Comparator<Transaction> byDate()
	{
		return (m1, m2) -> { return m1.getDate().compareTo(m2.getDate()); };
	}
}
