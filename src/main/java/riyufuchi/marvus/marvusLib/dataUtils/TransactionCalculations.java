package riyufuchi.marvus.marvusLib.dataUtils;

import java.math.BigDecimal;
import java.time.Month;
import java.util.LinkedList;
import java.util.function.Consumer;

import riyufuchi.marvus.marvusLib.data.MoneyCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.utils.DataBox;
import riyufuchi.sufuLib.gui.DialogHelper;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 28.06.2023
 * 
 * @author Riyufuchi
 */
public class TransactionCalculations
{
	private TransactionCalculations()
	{
	}
	
	/**
	 * Calculates incomes and outcomes in specified month and shows result -> income - outcome 
	 * 
	 * @param monthOrderNum
	 * @return
	 */
	public static Consumer<DataBox<Transaction>> incomeToOutcome(int monthOrderNum)
	{
		return data -> {
			BigDecimal spendings = new BigDecimal(0);
			BigDecimal income = new BigDecimal(0);
			BigDecimal zero = new BigDecimal(0);
			for(Transaction t : data)
			{
				if (t.getDate().getMonthValue() == monthOrderNum)
				{
					switch (t.getValue().compareTo(zero))
					{
						case 1 -> income = income.add(t.getValue());
						case -1 -> spendings = spendings.add(t.getValue());
						case 0 -> DialogHelper.warningDialog(null, "Zero value detected for: " + t.toString() + "\nSome data can be missing", "Zero money sum");
					}
				}
			}
			DialogHelper.informationDialog(null, (income + " " + spendings + " = "  + income.add(spendings)), "Ratio in " + Month.values()[monthOrderNum - 1].toString());
		};
	}

	public static LinkedList<MoneyCategory> categorizeMonth(DataBox<Transaction> data, int month)
	{
		LinkedList<MoneyCategory> list = new LinkedList<>();
		MoneyCategory holder = null;
		for (Transaction t : data)
		{
			if (t.getDate().getMonthValue() == month)
			{
				holder = new MoneyCategory(t);
				for (MoneyCategory mc : list)
				{
					if (mc.getName().equals(holder.getName()))
					{
						mc.add(t.getValue());
						holder = null;
						break;
					}
				}
				if(holder != null)
					list.add(holder);
			}
		}
		return list;
	}
}
