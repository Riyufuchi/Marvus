package riyufuchi.marvus.marvusLib.financialRecords;

import java.math.BigDecimal;

public record YearOverview(int year, BigDecimal[] income, BigDecimal[] outcome)
{
	public BigDecimal[] getTotalData()
	{
		BigDecimal yearIncome = new BigDecimal(0);
		BigDecimal yearOutcome = new BigDecimal(0);
		for (int i = 0; i < 12; i++)
		{
			yearIncome = yearIncome.add(income[i]);
			yearOutcome = yearOutcome.add(outcome[i]);
		}
		BigDecimal[] data = new BigDecimal[3];
		data[0] = yearIncome;
		data[1] = yearOutcome;
		data[2] = yearIncome.add(yearOutcome);
		return data;
	}
}
