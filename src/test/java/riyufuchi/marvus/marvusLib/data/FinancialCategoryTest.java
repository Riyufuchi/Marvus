package riyufuchi.marvus.marvusLib.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FinancialCategoryTest
{
	private Transaction t1, t2;
	
	public FinancialCategoryTest()
	{
		t1 = new Transaction("T1", "300.125", "17.05.2023", "");
		t2 = new Transaction("T2", "300.125", "17.05.2023", "Note");
	}
	
	@Test
	public void testToString01()
	{
		assertEquals("T1 0 CZK", new FinancialCategory("T1").toString());
	}
	
	@Test
	public void testToString02()
	{
		assertEquals("T1 300.125 CZK", new FinancialCategory(t1).toString());
	}
	
	@Test
	public void testToString03()
	{
		FinancialCategory fc = new FinancialCategory(t1);
		fc.add(t2);
		assertEquals("T1 600.250 CZK", fc.toString());
	}
}
