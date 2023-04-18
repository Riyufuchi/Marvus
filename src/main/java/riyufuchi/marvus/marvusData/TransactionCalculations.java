package riyufuchi.marvus.marvusData;

import java.math.BigDecimal;
import java.util.function.Consumer;

import riyufuchi.sufuLib.gui.DialogHelper;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 18.04.2023
 * 
 * @author Riyufuchi
 */
public class TransactionCalculations
{
	private TransactionCalculations()
	{
	}
	
	public static Consumer<DataBox<Transaction>> icomeToOutcome(int monthOrderNum)
	{
		return data -> {
			MoneyCalculations mc = new MoneyCalculations();
			BigDecimal spendings = new BigDecimal(0);
			BigDecimal income = new BigDecimal(0);
			for(Transaction t : data)
			{
				if (t.getDate().getMonthValue() == monthOrderNum)
				{
					mc.add(t.getMoneySum());
					if(t.getMoneySum().intValue() < 0)
						spendings = spendings.add(t.getMoneySum());
					else
						income = income.add(t.getMoneySum());
				}
			}
			DialogHelper.informationDialog(null, (income + " - " + spendings + " = "  + mc.getSum()), "Ratio");
		};
	}

}
