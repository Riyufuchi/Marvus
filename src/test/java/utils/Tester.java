package utils;

import static org.junit.Assert.*;
import static utils.TestCounter.counter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class Tester {

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
	public void testDoubleFormat04()
	{
		print(testName.getMethodName());
		Helper.checkDoubleFormat(" .o");
		fail();
	}

	@Test
	public void testDoubleFormat03()
	{
		print(testName.getMethodName());
		assertEquals("207.3", Helper.checkDoubleFormat(" 20o7., .3 "));
	}
}
