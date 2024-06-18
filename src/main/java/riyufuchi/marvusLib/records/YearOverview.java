package riyufuchi.marvusLib.records;

import java.math.BigDecimal;

public record YearOverview(int year, BigDecimal[] income, BigDecimal[] spendigs, BigDecimal totalIncome, BigDecimal totalOutcome, BigDecimal totalResult)
{
	@Deprecated
	public BigDecimal[] getTotalData()
	{
		BigDecimal yearIncome = new BigDecimal(0);
		BigDecimal yearOutcome = new BigDecimal(0);
		for (int i = 0; i < 12; i++)
		{
			yearIncome = yearIncome.add(income[i]);
			yearOutcome = yearOutcome.add(spendigs[i]);
		}
		BigDecimal[] data = new BigDecimal[3];
		data[0] = yearIncome;
		data[1] = yearOutcome;
		data[2] = yearIncome.add(yearOutcome);
		return data;
	}
}
