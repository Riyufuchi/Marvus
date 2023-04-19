package riyufuchi.marvus.app.utils;

import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.gui.windows.BudgetDataTable;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.MoneyCategory;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 19.04.2023
 * <hr>
 * Provides display modes for data
 * <hr>
 * @author Riyufuchi
 */
public class DataDisplayMode
{
	public DataDisplayMode()
	{
	}
	
	public static Consumer<DataBox<Transaction>> simpleList(BudgetDataTable bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int i = 0;
			for (Transaction t : data)
			{
				panel.add(FactoryComponent.newTextFieldCell(t.toString()), bdt.getGBC(0, i));
				i++;
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> categoryListByMonth(BudgetDataTable bdt)
	{
		return data -> {
			int month = 4;
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
							mc.add(t.getMoneySum().toString());
							holder = null;
							break;
						}
					}
					if(holder != null)
						list.add(holder);
				}
			}
			JPanel panel = bdt.getPane();
			month = 1;
			panel.add(FactoryComponent.newTextFieldCell("Category"), bdt.getGBC(0, 0));
			panel.add(FactoryComponent.newTextFieldCell("Sum"), bdt.getGBC(1, 0));
			for(MoneyCategory category : list)
			{
				panel.add(FactoryComponent.newTextFieldCell(category.getName()), bdt.getGBC(0, month));
				panel.add(FactoryComponent.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, month));
				month++;
			}
		};
	}
}
