package general.utils;

import static general.utils.TestCounter.counter;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import general.helpers.Helper;

public class HelperTest
{
	@Rule
	public TestName testName = new TestName();
	
	public static void print(String methodName)
	{
		System.out.println(String.format("%03d TestStaticFunctins.%s", counter++, methodName));
	}

	@Test
	public void test()
	{
		print(testName.getMethodName());
		//fail("Not yet implemented");
	}
	
	@Test(expected = NullPointerException.class)
	public void testDoubleFormat01()
	{
		print(testName.getMethodName());
		Helper.checkDoubleFormat(null);
		fail();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDoubleFormat02()
	{
		print(testName.getMethodName());
		Helper.checkDoubleFormat(" ");
		fail();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDoubleFormat03()
	{
		print(testName.getMethodName());
		Helper.checkDoubleFormat(" .o");
		fail();
	}
	
	@Test
	public void testDoubleFormat04()
	{
		print(testName.getMethodName());
		assertEquals("207.3", Helper.checkDoubleFormat(" 20o7., .3 "));
	}
	
	@Test
	public void testDoubleFormat05()
	{
		print(testName.getMethodName());
		assertEquals("10.50", Helper.checkDoubleFormat(" 10,.50"));
	}
}
