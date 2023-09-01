package riyufuchi.marvus.marvusLib.data;

import riyufuchi.marvus.marvusLib.dataUtils.MoneyCalculationsGeneric;

/**
 * Class representing transaction category and unites them into sum<br><br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 01.09.2023
 * 
 * @author Riyufuchi
 */
public class FinancialCategory extends MoneyCalculationsGeneric<Transaction>
{
	private String category;
	
	@Deprecated
	public FinancialCategory(String name)
	{
		super();
		this.category = name;
	}
	
	public FinancialCategory(String name, Transaction transaction)
	{
		super();
		this.category = name;
		if (transaction == null)
			transaction = new Transaction();
		add(transaction);
	}
	
	public FinancialCategory(Transaction transaction)
	{
		super();
		if (transaction == null)
			transaction = new Transaction();
		this.category = transaction.getName();
		add(transaction);
	}
	
	@Deprecated
	public String getName()
	{
		return category;
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
