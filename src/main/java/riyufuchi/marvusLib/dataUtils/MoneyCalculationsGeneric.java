package riyufuchi.marvusLib.dataUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;

import riyufuchi.marvusLib.data.Money;

/**
 * @author Riyufuchi
 * @version 1.2 - 09.10.2023
 * @since 1.56 - 21.08.2023
 */
public class MoneyCalculationsGeneric<E extends Money> extends LinkedList<E>
{
	public MoneyCalculationsGeneric()
	{
	}

	@Override
	public boolean add(E e)
	{
		if (e == null)
			return false;
		return super.add(e);
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
	
	/**
	 * Calculate average from list of BigDecimals and scale to 2 decimals and rounds half up
	 * 
	 * @return average rounded to 2 decimals and half up
	 */
	public BigDecimal getAverage()
	{
		if (size() == 0)
			return new BigDecimal(0);
		return getSum().divide(BigDecimal.valueOf(size()), 2, RoundingMode.HALF_UP);
	}
}
