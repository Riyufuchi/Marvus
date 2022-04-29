package workData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created On: 26.04.2022 <br>
 * Last Edit: 27.04.2022<br>
 * Class that handles money calculations wih use of BigDecimal
 * 
 * @author Riyufuchi
 * @version 1.0
 * @since 1.0
 */
public class Calculations
{
	private LinkedList<BigDecimal> moneyValues;
	
	public Calculations()
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
	
	/**
	 * Calculate average from list of BigDecimals and scale to 2 decimals and rounds up
	 * 
	 * @return average rounded to 2 decimals and up
	 */
	public BigDecimal getAverage()
	{
		return getSum().divide(BigDecimal.valueOf(moneyValues.size()), 2, RoundingMode.CEILING);
	}
	
}
