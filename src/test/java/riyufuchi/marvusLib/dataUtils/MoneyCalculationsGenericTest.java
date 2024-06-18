package riyufuchi.marvusLib.dataUtils;

import static org.junit.Assert.*;

import org.junit.Test;

import riyufuchi.marvusLib.data.Money;

public class MoneyCalculationsGenericTest
{
	private MoneyCalculationsGeneric<Money> cal;
	
	public MoneyCalculationsGenericTest()
	{
		cal = new MoneyCalculationsGeneric<>();
	}

	@Test
	public void testAdd01()
	{
		assertEquals(false, cal.add(null));
	}
	
	@Test
	public void testAdd02()
	{
		cal.add(null);
		assertEquals(0, cal.size());
	}

	@Test
	public void testAverage()
	{
		cal.add(new Money("2273"));
		cal.add(new Money("4"));
		assertEquals("1138.50", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage2()
	{
		cal.add(new Money("83"));
		cal.add(new Money("0.0"));
		cal.add(new Money("0.0"));
		assertEquals("27.67", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage4()
	{
		cal.add(new Money("100.25"));
		cal.add(new Money("1024.16"));
		cal.add(new Money("512"));
		cal.add(new Money("64"));
		cal.add(new Money("128"));
		cal.add(new Money("32.8"));
		assertEquals("310.20", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage5()
	{
		cal.add(new Money("100.255"));
		assertEquals("100.26", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage6()
	{
		assertEquals("0", cal.getAverage().toString());
	}
}
