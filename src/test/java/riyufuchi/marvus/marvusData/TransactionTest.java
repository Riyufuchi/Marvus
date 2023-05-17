package riyufuchi.marvus.marvusData;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TransactionTest
{
	private Transaction t1, t2, t2equal;
	
	public TransactionTest()
	{
		t1 = new Transaction("T1", "300.125", "17.05.2023", "");
		t2 = new Transaction("T1", "300.125", "17.05.2023", "Note");
		t2equal = new Transaction("T1", "300.125", "17.05.2023", "Note");
	}
	
	@Test
	public void testToStringSplit01()
	{
		assertEquals(t1.toString().split(";", 4).length, 4);
	}
	
	@Test
	public void testToStringSplit02()
	{
		String[] split = t2.toString().split(";", 4);
		assertEquals(split[3], "Note");
	}
	
	@Test
	public void testEquals01()
	{
		assertEquals(true, t2.equals(t2equal));
	}
	
	@Test
	public void testEquals02()
	{
		assertEquals(false, t1.equals(t2));
	}
	
	@Test
	public void testEquals03()
	{
		assertEquals(false, t1.equals(null));
	}
	
	@Test
	public void testEquals04()
	{
		assertEquals(true, t1.equals(t1));
	}
	
	@Test
	public void testHashCode01()
	{
		assertEquals(true, t1.hashCode() == t1.hashCode());
	}
	
	@Test
	public void testHashCode02()
	{
		assertEquals(true, t1.hashCode() != t2.hashCode());
	}
	
	@Test
	public void testHashCode03()
	{
		assertEquals(true, t2.hashCode() != t2equal.hashCode());
	}
}
