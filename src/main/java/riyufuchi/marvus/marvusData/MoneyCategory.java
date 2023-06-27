package riyufuchi.marvus.marvusData;

import riyufuchi.marvus.marvusLib.utils.MoneyCalculations;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 19.04.2023
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
	
	public MoneyCategory(Transaction transaction)
	{
		super();
		this.name = transaction.getName();
		add(transaction.getValue().toString());
	}
	
	public MoneyCategory(String name, String val)
	{
		super();
		this.name = name;
		add(val);
	}
	
	public String getName()
	{
		return name;
	}
}
