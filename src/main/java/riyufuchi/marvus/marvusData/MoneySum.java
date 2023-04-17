package riyufuchi.marvus.marvusData;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created On: 11.04.2022<br>
 * Last Edit: 17.04.2023
 * <hr>
 * MoneySum class represents sum of money.<br>Attribute "date" is here for compatibility with already programmed classes.
 * <hr>
 * @author Riyufuchi
 * @version 1.2
 * @since 1.0
 */
public class MoneySum implements Serializable
{ 
	private static final long serialVersionUID = 4827792392760337092L;
	private static int Static_ID = 0;
	private int ID;
	private BigDecimal moneySum;
	private String date;
	
	public MoneySum(double sum, String date)
	{
		this.ID = ++Static_ID;
		this.moneySum = BigDecimal.valueOf(sum);
		this.date = date;
	}
	
	public MoneySum(String sum, String date)
	{
		this.ID = ++Static_ID;
		this.moneySum = new BigDecimal(sum);
		this.date = date;
	}
	
	/**
	 * Technically equal to "money.toString().split(";");"
	 * 
	 * @return array of attributes
	 */
	public String[] getDataArray()
	{
		String[] data = new String[2];
		data[0] = moneySum.toPlainString();
		data[1] = date;
		return data;
	}
	
	public void setMoneySum(Double sum)
	{
		this.moneySum = BigDecimal.valueOf(sum);
	}

	public void setDate(String date)
	{
		this.date = date;
	}
	
	public int getID()
	{
		return ID;
	}

	public BigDecimal getMoneySum()
	{
		return moneySum;
	}

	public String getDate()
	{
		return date;
	}

	@Override
	public String toString()
	{
		return moneySum.toPlainString() + ";" + date;
	}
}