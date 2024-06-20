package riyufuchi.marvusLib.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import riyufuchi.sufuLib.interfaces.CSVable;

/**
 * Money class represents money<br><br>
 * 
 * Created On: 11.04.2022<br>
 * Last Edit: 03.10.2023
 * 
 * @author Riyufuchi
 * @version 1.9
 * @since 1.0
 */
public class Money implements Serializable, CSVable
{ 
	private static final long serialVersionUID = 4827792392760337092L;
	private static String defaultCurrency = "CZK";
	private BigDecimal value;
	private String currency;
	
	public Money(String value)
	{
		setValue(value);
		this.currency = defaultCurrency;
	}
	
	public Money(String value, String currency)
	{
		setValue(value);
		this.currency = currency;
	}
	
	
	public Money(double value)
	{
		setValue(String.valueOf(value));
		this.currency = defaultCurrency;
	}
	
	public Money(double value, String currency)
	{
		setValue(String.valueOf(value));
		this.currency = currency;
	}
	
	// SETTERS
	
	public static void setDefaultCurrency(String defCurr)
	{
		if (defCurr != null && !defCurr.isBlank())
			defaultCurrency = defCurr;
	}
	
	public void setValue(String value)
	{
		try
		{
			this.value = new BigDecimal(value);
		}
		catch (NumberFormatException e)
		{
			this.value = BigDecimal.valueOf(e.toString().length());
		}
	}
	
	public void setMoneySum(BigDecimal value)
	{
		this.value = value;
	}
	
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	// GETTERS
	
	public static String getDefaultCurrency()
	{
		return defaultCurrency;
	}
	
	/**
	 * Technically equal to "money.toString().split(";");"
	 * 
	 * @return array of attributes
	 */
	public String[] getDataArray()
	{
		String[] data = new String[2];
		data[0] = value.toPlainString();
		data[1] = currency;
		return data;
	}
	
	public BigDecimal getValue()
	{
		return value;
	}
	
	public String getCurrency()
	{
		return currency;
	}

	// OVERRIDE METHODS
	
	@Override
	public String toString()
	{
		return value.toPlainString() + ";" + currency;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(currency, value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Money))
			return false;
		Money other = (Money) obj;
		return Objects.equals(currency, other.currency) && Objects.equals(value, other.value);
	}

	@Override
	public String toCSV()
	{
		return value.toPlainString() + ";" + currency;
	}

	@Override
	public Money fromCSV(String[] arg)
	{
		return new Money(arg[0], arg[1]);
	}
}