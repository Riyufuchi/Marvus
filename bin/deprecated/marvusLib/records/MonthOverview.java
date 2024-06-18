package riyufuchi.marvus.marvusLib.records;

import java.math.BigDecimal;
import java.time.Month;

public record MonthOverview(Month month, BigDecimal income, BigDecimal spendings, BigDecimal avgIncome, BigDecimal avgSpendings)
{

}
