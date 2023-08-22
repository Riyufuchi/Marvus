package riyufuchi.marvus.marvusLib.data;

import riyufuchi.marvus.marvusLib.dataUtils.MoneyCalculationsGeneric;

/**
 * Class representing transaction category and unites them into sum<br><br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 21.08.2023
 * 
 * @author Riyufuchi
 */
public class FinancialCategory extends MoneyCalculationsGeneric<Transaction>
{
	private String name;
	
	public FinancialCategory(String name)
	{
		super();
		this.name = name;
	}
	
	public FinancialCategory(Transaction transaction)
	{
		super();
		if (transaction == null)
			transaction = new Transaction();
		this.name = transaction.getName();
		add(transaction);
	}
	
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name + " " + getSum();
	}
}
