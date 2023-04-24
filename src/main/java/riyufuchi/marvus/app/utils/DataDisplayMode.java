package riyufuchi.marvus.app.utils;

import java.time.Month;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.gui.windows.BudgetDataTable;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.DateUtils;
import riyufuchi.marvus.marvusData.MoneyCategory;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 24.04.2023
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
	
	/**
	 * Simple list ordered by ID assigned during loading from file.
	 * 
	 * @param budgetDataTable
	 * @return
	 */
	public static Consumer<DataBox<Transaction>> simpleList(BudgetDataTable budgetDataTable)
	{
		return data -> {
			JPanel panel = budgetDataTable.getPane();
			data.stream().forEach(t -> panel.add(FactoryComponent.newTextFieldCell(t.toString()), budgetDataTable.getGBC(0, t.getID())));
			//for (Transaction t : data)
				//panel.add(FactoryComponent.newTextFieldCell(t.toString()), bdt.getGBC(0, t.getID()));
		};
	}
	
	public static Consumer<DataBox<Transaction>> simpleOrderableList(BudgetDataTable bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int y = 0;
			for (Transaction t : data)
			{
				panel.add(FactoryComponent.newTextFieldCell(t.toString()), bdt.getGBC(0, y++));
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> monthList(BudgetDataTable bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int y = 0;
			int prevMonth = data.getList().getFirst().getDate().getMonthValue();
			for (Transaction t : data)
			{
				if (prevMonth < t.getDate().getMonthValue())
				{
					prevMonth = t.getDate().getMonthValue();
					y = 0;
				}
				panel.add(FactoryComponent.newTextFieldCell(t.toString()), bdt.getGBC(prevMonth, ++y));
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> categoryListByMonth(BudgetDataTable bdt)
	{
		return data -> {
			int month = DateUtils.showMonthChooser(bdt).getValue();
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
			panel.add(FactoryComponent.newTextFieldCell("Month"), bdt.getGBC(0, 0));
			panel.add(FactoryComponent.newTextFieldCell(Month.values()[month - 1].toString()), bdt.getGBC(1, 0));
			panel.add(FactoryComponent.newTextFieldCell("Category"), bdt.getGBC(0, 1));
			panel.add(FactoryComponent.newTextFieldCell("Sum"), bdt.getGBC(1, 1));
			month = 2;
			for(MoneyCategory category : list)
			{
				panel.add(FactoryComponent.newTextFieldCell(category.getName()), bdt.getGBC(0, month));
				panel.add(FactoryComponent.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, month));
				month++;
			}
		};
	}
}
