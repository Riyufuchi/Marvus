package riyufuchi.marvus.appTesting;

import riyufuchi.marvus.marvusLib.data.Transaction;

/**
 * This class contains test data so each test don't have to initialize them
 * 
 * @author Riyufuchi
 */
public class TestData
{
	public static final Transaction transaction =  new Transaction();
	public static final Transaction transaction1 =  new Transaction("T1", "C1", "300.125", "16.05.2023", "");
	public static final Transaction transaction2 = new Transaction("T2", "C2", "300.125", "17.05.2023", "Note");
	public static final Transaction transaction2_equal = new Transaction("T2", "C2", "300.125", "17.05.2023", "Note");
}
