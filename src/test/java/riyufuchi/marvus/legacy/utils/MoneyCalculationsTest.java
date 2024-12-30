package riyufuchi.marvus.legacy.utils;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class MoneyCalculationsTest
{
	private MoneyCalculations cal;
	
	public MoneyCalculationsTest()
	{
		cal = new MoneyCalculations();
	}

	@Test(expected = NullPointerException.class)
	public void testAdd01() {
		BigDecimal bg = null;
		cal.add(bg);
		fail();
	}
	
	@Test(expected = NumberFormatException.class)
	public void testAdd02() {
		cal.add(" ");
		fail();
	}

	@Test
	public void testAverage() {
		cal.add("2273");
		cal.add("4");
		assertEquals("1138.50", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage2() {
		cal = new MoneyCalculations();
		cal.add("83");
		cal.add("0.0");
		cal.add("0.0");
		assertEquals("27.67", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage4() {
		cal = new MoneyCalculations();
		cal.add("100.25");
		cal.add("1024.16");
		cal.add("512");
		cal.add("64");
		cal.add("128");
		cal.add("32.8");
		assertEquals("310.20", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage5() {
		cal = new MoneyCalculations();
		cal.add("100.255");
		assertEquals("100.26", cal.getAverage().toString());
	}
	
	@Test
	public void testAverage6()
	{
		cal = new MoneyCalculations();
		assertEquals("0", cal.getAverage().toString());
	}
}
