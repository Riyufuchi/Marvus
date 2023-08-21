package riyufuchi.marvus.marvusLib.data;

import riyufuchi.marvus.marvusLib.dataUtils.MoneyCalculations;

/**
 * Class representing transaction category and unites them into sum<br><br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 21.08.2023
 * 
 * @author Riyufuchi
 */
public class MoneyCategory extends MoneyCalculations
{
	private String name;
	
	public MoneyCategory(String name)
	{
		super();
		this.name = name;
	}
	
	public MoneyCategory(String name, String val)
	{
		super();
		this.name = name;
		add(val);
	}
	
	public MoneyCategory(Transaction transaction)
	{
		super();
		if (transaction == null)
			transaction = new Transaction();
		this.name = transaction.getName();
		add(transaction.getValue().toString());
	}
	
	
	public String getName()
	{
		return name;
	}
}
