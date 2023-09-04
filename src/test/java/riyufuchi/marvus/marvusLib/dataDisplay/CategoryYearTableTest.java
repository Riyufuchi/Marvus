package riyufuchi.marvus.marvusLib.dataDisplay;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import riyufuchi.marvus.appTesting.TestData;
import riyufuchi.marvus.marvusLib.data.Transaction;

public class CategoryYearTableTest
{
	private CategoryYearTable ct = new CategoryYearTable(null);
	
	public CategoryYearTableTest()
	{
		this.ct = new CategoryYearTable(null);
	}
	
	@Test
	public void testAdd01()
	{
		ct.add(TestData.transaction1);
		assertEquals(TestData.transaction1, ct.get(4, 0).get(0));
	}
	
	@Test
	public void testAdd02()
	{
		ct.add(TestData.transaction1);
		ct.add(TestData.transaction1);
		assertEquals(2, ct.get(4, 0).size());
	}
	
	@Test
	public void testSet01()
	{
		ct.add(TestData.transaction1);
		Transaction t = TestData.transaction1;
		t.setValue("1000");
		ct.set(t);
		t = null;
		assertEquals(1000, ct.get(4, 0).getFirst().getValue().intValueExact());
	}
	@Test
	public void testSet02()
	{
		ct.add(TestData.transaction1);
		Transaction t = TestData.transaction1;
		t.setValue("1000");
		ct.set(t);
		assertEquals(1, ct.get(4, 0).size());
	}
	
	@Test
	public void testRemove01()
	{
		ct.add(TestData.transaction1);
		ct.remove(TestData.transaction1);
		assertEquals(0, ct.get(4, 0).size());
	}
	
	@Test
	public void testSize01()
	{
		ct.add(TestData.transaction1);
		assertEquals(1, ct.size());
	}
	
	@Test
	public void testSize02()
	{
		ct.add(TestData.transaction1);
		ct.add(TestData.transaction2);
		assertEquals(2, ct.size());
	}
	
	@Test
	public void testSize03()
	{
		ct.add(TestData.transaction1);
		ct.add(TestData.transaction2);
		ct.remove(TestData.transaction2_equal);
		assertEquals(1, ct.size());
	}
}
