package workData;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created On: 11.04.2022
 * Last Edit: 11.04.2022
 * @author Riyufuchi
 * @version 1.0
 * @since 1.0
 */

public class Money implements Serializable
{ 
	private static final long serialVersionUID = 4827792392760337092L;
	private static int Static_ID = 0;
	private int ID;
	private BigDecimal moneySum;
	private String date;
	
	public Money(double sum, String date)
	{
		this.ID = ++Static_ID;
		this.moneySum = BigDecimal.valueOf(sum);
		this.date = date;
	}
	
	public Money(String sum, String date)
	{
		this.ID = ++Static_ID;
		this.moneySum = new BigDecimal(sum);
		this.date = date;
	}
	
	public Money(int id, String sum, String date)
	{
		this.ID = id;
		this.moneySum = new BigDecimal(sum);
		this.date = date;
	}
	
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
		return date + ";" + moneySum.toPlainString();
	}
	
}