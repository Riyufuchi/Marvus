package riyufuchi.marvus.marvusData;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import riyufuchi.marvus.marvusLib.utils.DateUtils;

/**
 * Created On: 11.04.2022<br>
 * Last Edit: 18.04.2023
 * <hr>
 * MoneySum class represents sum of money
 * <hr>
 * @author Riyufuchi
 * @version 1.4
 * @since 1.0
 */
public class MoneySum implements Serializable
{ 
	private static final long serialVersionUID = 4827792392760337092L;
	private static int Static_ID = 0;
	private int ID;
	private BigDecimal moneySum;
	private String dateString;
	private LocalDateTime date;
	
	public MoneySum(double sum, String date)
	{
		this.ID = ++Static_ID;
		setMoneySum(String.valueOf(sum));
		setDate(date);
	}
	
	public MoneySum(String sum, String date)
	{
		this.ID = ++Static_ID;
		setMoneySum(sum);
		setDate(date);
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
		data[1] = dateString;
		return data;
	}
	
	public void setMoneySum(String sum)
	{
		try
		{
			this.moneySum = new BigDecimal(sum);
		}
		catch (NumberFormatException e)
		{
			this.moneySum = BigDecimal.valueOf(e.toString().length());
		}
	}
	
	public void setMoneySum(BigDecimal sum)
	{
		this.moneySum = sum;
	}

	public void setDate(String date)
	{
		this.dateString = DateUtils.dateToString(date);
		this.date = DateUtils.toLocalDateTime(dateString);
	}
	
	public int getID()
	{
		return ID;
	}

	public BigDecimal getMoneySum()
	{
		return moneySum;
	}

	public String getStringDate()
	{
		return dateString;
	}
	
	public LocalDateTime getDate()
	{
		return date;
	}

	@Override
	public String toString()
	{
		return moneySum.toPlainString() + ";" + dateString;
	}
}