package riyufuchi.marvus.app.utils;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.utils.DataBox;

public class MarvusUtils
{
	private MarvusUtils() {}
	
	public static void fixCategory(DataBox<Transaction> data)
	{
		data.forEach(transaction -> transaction.setCategory(TransactionCategory.categories[getCategoryID(transaction.getName())]));
	}
	
	private static int getCategoryID(String name)
	{
		int i = 0;
		for (String s : TransactionCategory.names)
			if (name.equals(s))
				return i;
			else
				i++;
		return 0;
	}
}
