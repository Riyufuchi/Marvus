package riyufuchi.marvusLib.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TransactionTest
{
	private Transaction t1, t2, t2equal;
	
	public TransactionTest()
	{
		t1 = new Transaction("T1", "C1", "300.125", "16.05.2023", "");
		t2 = new Transaction("T2", "C2", "300.125", "17.05.2023", "Note");
		t2equal =  new Transaction("T2", "C2", "300.125", "17.05.2023", "Note");
	}
	
	@Test
	public void testToStringSplit01()
	{
		assertEquals(6, t1.toString().split(";", 6).length);
	}
	
	@Test
	public void testToStringSplit02()
	{
		String[] split = t2.toString().split(";", 6);
		assertEquals(split[5], "Note");
	}
	
	@Test
	public void testEquals01()
	{
		assertEquals(false, t2.equals(t2equal));
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
	public void testEquals05()
	{
		Transaction t = new Transaction();
		t.setValue("100");
		assertEquals(true, t.equals(new Transaction()));
	}
	
	@Test
	public void testEquals06()
	{
		Transaction t = t2equal;
		t.setValue("100");
		assertEquals(false, t.equals(t2));
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
