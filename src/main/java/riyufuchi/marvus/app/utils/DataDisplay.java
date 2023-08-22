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
import riyufuchi.marvus.app.windows.RemoveDialog;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.utils.DataBox;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * Provides display utility functions for data<br><br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 28.06.2023
 * 
 * @author Riyufuchi
 */
public class DataDisplay
{
	private DataDisplay()
	{}
	
	public static void showExtednedInfo(Transaction t, BudgetDataTable budgetDataTable, MouseEvent mEvt)
	{
		if(SwingUtilities.isLeftMouseButton(mEvt))
		{
			new EditDialog(budgetDataTable, t).showDialog();
		}
		else
		{
			new RemoveDialog(budgetDataTable, t).showDialog();
		}
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
			int[] columnHeight = new int[12];
			for (int i = 0; i < 12; i++)
				columnHeight[i] = 0;
			SufuWindowTools.createTableRow(bdt, 1, 0, (Object[])Month.values());
			for (Transaction t : data)
			{
				panel.add(FactoryComponent.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, bdt, evt)), bdt.getGBC(t.getDate().getMonthValue(), ++columnHeight[t.getDate().getMonthValue()]));
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> categoryListByMonth(BudgetDataTable bdt)
	{
		return data -> {
			int month = DateUtils.showMonthChooser(bdt).getValue();
			LinkedList<FinancialCategory> list = TransactionCalculations.categorizeMonth(bdt.getDataBox(), month);
			JPanel panel = bdt.getPane();
			SufuWindowTools.createTableRow(bdt, 0, "Month", Month.values()[month - 1]);
			SufuWindowTools.createTableRow(bdt, 1, "Category", "Sum");
			month = 2; // Substitute for Y coordinate
			for(FinancialCategory category : list)
			{
				panel.add(FactoryComponent.newTextFieldCell(category.getName()), bdt.getGBC(0, month));
				panel.add(FactoryComponent.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, month));
				month++;
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> categoryMonthList(BudgetDataTable bdt)
	{
		return data -> {
			int month = DateUtils.showMonthChooser(bdt).getValue();
			LinkedList<FinancialCategory> list = TransactionCalculations.categorizeMonth(bdt.getDataBox(), month);
			JPanel panel = bdt.getPane();
			SufuWindowTools.createTableRow(bdt, 0, "Month", Month.values()[month - 1]);
			SufuWindowTools.createTableRow(bdt, 1, "Category", "Sum");
			month = 2; // Substitute for Y coordinate
			for(FinancialCategory category : list)
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
					switch (t.getValue().compareTo(zero))
					{
						case 1 -> income[t.getDate().getMonthValue() - 1] = income[t.getDate().getMonthValue() - 1].add(t.getValue());
						case -1 -> outcome[t.getDate().getMonthValue() - 1] = outcome[t.getDate().getMonthValue() - 1].add(t.getValue());
						case 0 -> DialogHelper.warningDialog(bdt, "Zero value detected for: " + t.toString() + "\nSome data can be missing", "Zero money sum");
					}
				}
			}
			JPanel panel = bdt.getPane();
			SufuWindowTools.createTableColumn(bdt, 0 , String.valueOf(year), "Income", "Outcome", "Total");
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
			SufuWindowTools.createTableColumn(bdt, 13 , "Year total", yearIncome, yearOutcome, yearIncome.add(yearOutcome));
		};
	}
}
