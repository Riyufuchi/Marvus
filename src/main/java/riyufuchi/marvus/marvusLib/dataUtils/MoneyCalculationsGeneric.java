package riyufuchi.marvus.marvusLib.dataUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;

import riyufuchi.marvus.marvusLib.data.Money;

/**
 * Created On: 21.08.2023<br>
 * Last Edit: 21.08.20233
 * 
 * @author Riyufuchi
 */
public class MoneyCalculationsGeneric<E extends Money> extends LinkedList<E>
{
	public MoneyCalculationsGeneric()
	{
		
	}
	
	/**
	 * Calculates sum
	 * 
	 * @return sum of BigDecimals
	 */
	public BigDecimal getSum()
	{
		BigDecimal sum = BigDecimal.valueOf(0);
		Iterator<E> it = iterator();
		while(it.hasNext())
			sum = sum.add(it.next().getValue());
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
		return getSum().divide(BigDecimal.valueOf(size()), 2, RoundingMode.HALF_UP);
	}
}
