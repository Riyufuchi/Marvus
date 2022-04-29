package utils;

import static org.junit.Assert.*;

import org.junit.Test;

import workData.Calculations;

public class CalculationsTest
{
	private Calculations cal;
	
	public CalculationsTest()
	{
		cal = new Calculations();
	}

	@Test(expected = NullPointerException.class)
	public void testAdd() {
		cal.add(null);
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
		cal = new Calculations();
		cal.add("83");
		cal.add("0.0");
		cal.add("0.0");
		assertEquals("27.67", cal.getAverage().toString());
	}
	
	@Test(expected = ArithmeticException.class)
	public void testAverage3() {
		cal = new Calculations();
		cal.getAverage();
		fail();
	}

}
