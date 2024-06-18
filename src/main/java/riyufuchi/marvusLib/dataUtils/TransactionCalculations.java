package riyufuchi.marvusLib.dataUtils;

import java.math.BigDecimal;
import java.time.Month;
import java.util.function.Consumer;

import javax.swing.JFrame;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 07.10.2023
 * @since 18.04.2023
 */
public class TransactionCalculations
{
	private TransactionCalculations()
	{
	}
	
	/**
	 * Calculates incomes and outcomes in specified month and shows result -> income - outcome 
	 * 
	 * @param month
	 * @return
	 */
	public static Consumer<Iterable<Transaction>> incomeToSpendings(JFrame parentFrame, Month month)
	{
		return data -> {
			if (month == null)
				return;
			BigDecimal spendings = new BigDecimal(0);
			BigDecimal income = new BigDecimal(0);
			BigDecimal zero = new BigDecimal(0);
			int monthNum = month.getValue();
			for(Transaction t : data)
			{
				if (t.getDate().getMonthValue() == monthNum)
				{
					switch (t.getValue().compareTo(zero))
					{
						case 1 -> income = income.add(t.getValue());
						case -1 -> spendings = spendings.add(t.getValue());
						case 0 -> SufuDialogHelper.warningDialog(parentFrame, "Zero value detected for: " + t.toString() + "\nSome data can be missing", "Zero money value");
					}
				}
			}
			SufuDialogHelper.informationDialog(parentFrame, (income + " " + spendings + " = "  + income.add(spendings)), "Ratio in " + month.toString());
		};
	}
}
