package riyufuchi.marvus.app.utils;

import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.Month;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import riyufuchi.marvus.app.windows.BudgetDataTable;
import riyufuchi.marvus.app.windows.EditDialog;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.MoneyCategory;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 02.05.2023
 * <hr>
 * Provides display utility functions for data
 * <hr>
 * @author Riyufuchi
 */
public class DataDisplay
{
	private DataDisplay()
	{
	}
	
	private static void showExtednedInfo(Transaction t, BudgetDataTable budgetDataTable, MouseEvent mEvt)
	{
		//DialogHelper.informationDialog(budgetDataTable, "ID: " + t.getID() + " -> " + t.toString(), "Info for ID: " + t.getID());
		if(SwingUtilities.isLeftMouseButton(mEvt))
		{
			EditDialog ed = new EditDialog(budgetDataTable, t);
			ed.showDialog();
		}
		else
		{
			// TODO: Implement proper delete dialog
			if(DialogHelper.yesNoDialog(budgetDataTable, "Delete ID: " + t.getID() + " " + t.toString(), "Delete " + t.getID()) == 0)
			{
				budgetDataTable.getDataBox().getList().remove(t.getID() - 1); // ID starts from 1
				budgetDataTable.refresh();
			}
		}
	}
	
	/**
	 * Creates column of non editable JTextFileds
	 * 
	 * @param panel destination panel - for optimization if this method is used in cycle
	 * @param bdt BudgetDataTable
	 * @param x coordinate
	 * @param data to be displayed using toString() method
	 */
	public static void createColumn(JPanel panel, BudgetDataTable bdt, int x, Object ... data)
	{
		int y = 0;
		for (Object o : data)
			panel.add(FactoryComponent.newTextFieldCell(o.toString()), bdt.getGBC(x, y++));
	}
	
	/**
	 * Creates row of non editable JTextFileds
	 * 
	 * @param panel destination panel - for optimization if this method is used in cycle
	 * @param bdt BudgetDataTable
	 * @param y coordinate
	 * @param data to be displayed using toString() method
	 */
	public static void createRow(JPanel panel, BudgetDataTable bdt, int y, Object ... data)
	{
		int x = 0;
		for (Object o : data)
			panel.add(FactoryComponent.newTextFieldCell(o.toString()), bdt.getGBC(x++, y));
	}
	
	// CONSUMERS
	
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
			data.stream().forEach(t -> panel.add(FactoryComponent.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, budgetDataTable, evt)),
					budgetDataTable.getGBC(0, t.getID())));
		};
	}
	
	public static Consumer<DataBox<Transaction>> simpleOrderableList(BudgetDataTable bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int y = 0;
			for (Transaction t : data)
			{
				panel.add(FactoryComponent.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, bdt, evt)), bdt.getGBC(0, y++));
			}
		};
	}
	
	/**
	 * Each month have its column
	 * 
	 * @param bdt BudgetDataTable
	 * @return Display mode consumer
	 */
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
				panel.add(FactoryComponent.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, bdt, evt)), bdt.getGBC(prevMonth, ++y));
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
			createRow(panel, bdt, 0, "Month", Month.values()[month - 1]);
			createRow(panel, bdt, 1, "Category", "Sum");
			month = 2; // Substitute for Y coordinate
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
			createColumn(panel, bdt, 0 , String.valueOf(year), "Income", "Outcome", "Total");
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
			BigDecimal yearIncome = new BigDecimal(0);
			BigDecimal yearOutcome = new BigDecimal(0);
			for (int i = 0; i < 12; i++)
			{
				yearIncome = yearIncome.add(income[i]);
				yearOutcome = yearOutcome.add(outcome[i]);
			}
			createColumn(panel, bdt, 13 , "Year total", yearIncome, yearOutcome, yearIncome.add(yearOutcome));
		};
	}
}
