package riyufuchi.marvus.marvusLib.legacy;

import java.awt.event.MouseEvent;
import java.time.Month;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.app.windows.dialogs.EditDialog;
import riyufuchi.marvus.app.windows.dialogs.RemoveDialog;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.YearOverviewTable;
import riyufuchi.marvus.marvusLib.dataStorage.DataBox;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * Provides display utility functions for data<br><br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 08.09.2023
 * 
 * @author Riyufuchi
 */
@Deprecated
public class DataDisplay
{
	private DataDisplay()
	{}
	
	public static void showExtednedInfo(Transaction t, MarvusDataWindow budgetDataTable, MouseEvent mEvt)
	{
		if(SwingUtilities.isLeftMouseButton(mEvt))
		{
			new EditDialog(budgetDataTable, t).showDialog();
		}
		else if (SwingUtilities.isRightMouseButton(mEvt))
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
	public static Consumer<DataBox<Transaction>> simpleList(MarvusDataWindow budgetDataTable)
	{
		return data -> {
			JPanel panel = budgetDataTable.getPane();
			data.stream().forEach(t -> panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, budgetDataTable, evt)),
					budgetDataTable.getGBC(0, t.getID())));
		};
	}
	
	public static Consumer<DataBox<Transaction>> simpleOrderableList(MarvusDataWindow bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int y = 0;
			for (Transaction t : data)
			{
				panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, bdt, evt)), bdt.getGBC(0, y++));
			}
		};
	}
	
	/**
	 * Each month have its column
	 * 
	 * @param bdt BudgetDataTable
	 * @return Display mode consumer
	 */
	public static Consumer<DataBox<Transaction>> monthList(MarvusDataWindow bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int[] columnHeight = new int[12];
			for (int i = 0; i < 12; i++)
				columnHeight[i] = 0;
			SufuWindowTools.createTableRow(bdt, 1, 0, (Object[])Month.values());
			for (Transaction t : data)
			{
				panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, bdt, evt)), bdt.getGBC(t.getDate().getMonthValue(), ++columnHeight[t.getDate().getMonthValue()]));
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> categoryListByMonth(MarvusDataWindow bdt)
	{
		return data -> {
			int month = DateUtils.showMonthChooser(bdt).getValue();
			LinkedList<FinancialCategory> list = bdt.getTable().getCategorizedMonth(month);
			JPanel panel = bdt.getPane();
			SufuWindowTools.createTableRow(bdt, 0, "Month", Month.values()[month - 1]);
			SufuWindowTools.createTableRow(bdt, 1, "Name", "Sum");
			month = 2; // Substitute for Y coordinate
			for(FinancialCategory category : list)
			{
				panel.add(SufuFactory.newTextFieldCell(category.getCategory()), bdt.getGBC(0, month));
				panel.add(SufuFactory.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, month++));
				//month++;
			}
		};
	}

	public static Consumer<DataBox<Transaction>> categoryYearList(MarvusDataWindow bdt)
	{
		return data -> {
			//LinkedList<FinancialCategory> list = TransactionCalculations.categorizeYearByCategories(data);
			JPanel panel = bdt.getPane();
			SufuWindowTools.createTableRow(bdt, 1, "Category", "Sum");
			int y = 2;
			for(FinancialCategory category : TransactionCalculations.categorizeYearByCategories(data))
			{
				panel.add(SufuFactory.newTextFieldCell(category.getCategory()), bdt.getGBC(0, y));
				panel.add(SufuFactory.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, y++));
				//y++;
			}
		};
	}
	
	public static Consumer<DataBox<Transaction>> categoryByMonthInYear(MarvusDataWindow bdt)
	{
		return data -> bdt.displayData();
	}
	
	public static Consumer<DataBox<Transaction>> yearOverview(MarvusDataWindow bdt)
	{
		return data -> {
			new YearOverviewTable(bdt, bdt.getTable(), 2023).displayData();;
		};
	}
}
