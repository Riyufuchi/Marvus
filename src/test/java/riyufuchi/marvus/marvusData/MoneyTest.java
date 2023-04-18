package riyufuchi.marvus.marvusData;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoneyTest
{
	private MoneySum moneySum;
	private String expectedDate = "01.02.2018";

	public MoneyTest()
	{
		moneySum = new MoneySum("500.5", "1.2.2018");
	}

	@Test
	public void testToString01()
	{
		String[] mArr = moneySum.getDataArray();
		String connected = "";
		int max = mArr.length - 1;
		for(int i = 0; i < max; i++)
			connected += mArr[i] + ";" + mArr[i+1];
		assertEquals(moneySum.toString(), connected);
	}
	
	@Test
	public void testDateFormat01()
	{
		assertEquals(expectedDate, moneySum.getStringDate());
	}
	
	@Test
	public void testDateFormat02()
	{
		MoneySum m = new MoneySum(100, "11112018");
		assertEquals("10.10.1601", m.getStringDate());
	}
	
	@Test
	public void testDateFormat03()
	{
		MoneySum m = new MoneySum(100, expectedDate);
		assertEquals(expectedDate, m.getStringDate());
	}
}
