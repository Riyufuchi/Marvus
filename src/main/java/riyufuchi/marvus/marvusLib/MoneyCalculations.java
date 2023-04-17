package riyufuchi.marvus.marvusLib;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created On: 26.04.2022 <br>
 * Last Edit: 17.04.2023<br>
 * Class that handles money calculations with use of BigDecimal
 * 
 * @author Riyufuchi
 * @version 1.2
 * @since 1.0
 */
public class MoneyCalculations
{
	private LinkedList<BigDecimal> moneyValues;
	
	public MoneyCalculations()
	{
		this.moneyValues = new LinkedList<>();
	}
	
	/**
	 * Adds and convert value of double inputed as String into BigDecial<br>
	 * Note: when input string don't represent a double value, exception is thrown
	 * 
	 * @param input double as string
	 * @throws NullPointerException
	 * @throws NumberFormatException when input is not double value representation
	 */
	public void add(String input) throws NullPointerException, NumberFormatException
	{
		moneyValues.add(new BigDecimal(Objects.requireNonNull(input)));
	}
	
	/**
	 * Calculates sum
	 * 
	 * @return sum of BigDecimals
	 */
	public BigDecimal getSum()
	{
		BigDecimal sum = BigDecimal.valueOf(0);
		Iterator<BigDecimal> it = moneyValues.iterator();
		while(it.hasNext())
			sum = sum.add(it.next());
		return sum;
	}
	
	//TODO: Should money be rounded normally or always up?
	/**
	 * Calculate average from list of BigDecimals and scale to 2 decimals and rounds half up
	 * 
	 * @return average rounded to 2 decimals and half up
	 */
	public BigDecimal getAverage()
	{
		return getSum().divide(BigDecimal.valueOf(moneyValues.size()), 2, RoundingMode.HALF_UP);
	}
	
}
