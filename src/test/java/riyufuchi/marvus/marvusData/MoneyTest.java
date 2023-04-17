package riyufuchi.marvus.marvusData;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoneyTest
{
	public MoneyTest()
	{
	}

	@Test
	public void testToString() {
		MoneySum m = new MoneySum("500.5", "1.1.2018");
		String[] mArr = m.getDataArray();
		String connected = "";
		int max = mArr.length - 1;
		for(int i = 0; i < max; i++)
			connected += mArr[i] + ";" + mArr[i+1];
		assertEquals(m.toString(), connected);
	}
}
