package riyufuchi.marvusLib.data;

import riyufuchi.marvusLib.dataUtils.MoneyCalculationsGeneric;

/**
 * Class representing transaction category and unites them into sum<br><br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 01.10.2023
 * 
 * @author RiyufuchiW
 */
public class FinancialCategory extends MoneyCalculationsGeneric<Transaction>
{
	private String category;
	
	public FinancialCategory(Transaction transaction)
	{
		super();
		if (transaction == null)
			transaction = new Transaction();
		this.category = transaction.getName();
		add(transaction);
	}
	
	public FinancialCategory(String name, Transaction transaction)
	{
		super();
		this.category = name;
		if (transaction == null)
			transaction = new Transaction();
		add(transaction);
	}
	
	public String getCategory()
	{
		return category;
	}

	@Override
	public String toString()
	{
		if (!isEmpty())
			return category + " " + getSum() + " " + getFirst().getCurrency();
		return category + " " + getSum() + " " + Money.getDefaultCurrency();
	}
}
