package riyufuchi.marvus.app.utils;

import java.math.BigDecimal;
import java.time.Month;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.gui.windows.BudgetDataTable;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.DateUtils;
import riyufuchi.marvus.marvusData.MoneyCategory;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.sufuLib.gui.DialogHelper;
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
	
	private static void showExtednedInfo(Transaction t, BudgetDataTable budgetDataTable)
	{
		DialogHelper.informationDialog(budgetDataTable, "ID: " + t.getID() + " -> " + t.toString(), "Info for ID: " + t.getID());
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
			data.stream().forEach(t -> panel.add(FactoryComponent.newTextFieldCell(t.toString(), fe -> showExtednedInfo(t, budgetDataTable)), budgetDataTable.getGBC(0, t.getID())));
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
				panel.add(FactoryComponent.newTextFieldCell(t.toString(), fe -> showExtednedInfo(t, bdt)), bdt.getGBC(0, y++));
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
				panel.add(FactoryComponent.newTextFieldCell(t.toString(), fe -> showExtednedInfo(t, bdt)), bdt.getGBC(prevMonth, ++y));
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
	
	public static Consumer<DataBox<Transaction>> yearOverview(BudgetDataTable bdt)
	{
		return data -> {
			int year = 2023;
			BigDecimal[] income = new BigDecimal[12];
			BigDecimal[] outcome = new BigDecimal[12];
			BigDecimal zero = new BigDecimal(0);
			for (int i = 0; i < 12; i++)
			{
				income[i] = new BigDecimal(0);
				outcome[i] = new BigDecimal(0);
			}
			for (Transaction t : data)
			{
				if (t.getDate().getYear() == year)
				{
					switch (t.getMoneySum().compareTo(zero))
					{
						case 1 -> income[t.getDate().getMonthValue() - 1] = income[t.getDate().getMonthValue() - 1].add(t.getMoneySum());
						case -1 -> outcome[t.getDate().getMonthValue() - 1] = outcome[t.getDate().getMonthValue() - 1].add(t.getMoneySum());
						case 0 -> DialogHelper.warningDialog(bdt, "Zero value detected for: " + t.toString() + "\nSome data can be missing", "Zero money sum");
					}
				}
			}
			JPanel panel = bdt.getPane();
			panel.add(FactoryComponent.newTextFieldCell(String.valueOf(year)), bdt.getGBC(0, 0));
			panel.add(FactoryComponent.newTextFieldCell("Income"), bdt.getGBC(0, 1));
			panel.add(FactoryComponent.newTextFieldCell("Outcome"), bdt.getGBC(0, 2));
			panel.add(FactoryComponent.newTextFieldCell("Total"), bdt.getGBC(0, 3));
			Month[] months = Month.values();
			int xPos = 0;
			for (int x = 1; x < 13; x++)
			{
				panel.add(FactoryComponent.newTextFieldCell(months[xPos].toString()), bdt.getGBC(x, 0));
				panel.add(FactoryComponent.newTextFieldCell(income[xPos].toString()), bdt.getGBC(x, 1));
				panel.add(FactoryComponent.newTextFieldCell(outcome[xPos].toString()), bdt.getGBC(x, 2));
				panel.add(FactoryComponent.newTextFieldCell((income[xPos].add(outcome[xPos]).toString())), bdt.getGBC(x, 3)); // outcome is already negative
				xPos++;
			}
		};
	}
}
