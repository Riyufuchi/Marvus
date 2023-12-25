package riyufuchi.marvus.marvusLib.legacy;

import java.awt.event.MouseEvent;
import java.time.Month;
import java.util.Collection;
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
import riyufuchi.marvus.marvusLib.dataStorage.MarvusDataBox;
import riyufuchi.marvus.marvusLib.dataStorage.MarvusDataTableCategorized;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * Provides display utility functions for data<br><br>
 *
 * @author Riyufuchi
 * @version 25.12.2023
 * @since 18.04.2023
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
	public static Consumer<MarvusDataBox<Transaction>> simpleList(MarvusDataWindow budgetDataTable)
	{
		return data -> {
			JPanel panel = budgetDataTable.getPane();
			data.stream().forEach(t -> panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, budgetDataTable, evt)),
					budgetDataTable.getGBC(0, t.getID())));
		};
	}
	
	public static Consumer<MarvusDataBox<Transaction>> simpleOrderableList(MarvusDataWindow bdt)
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
	public static Consumer<MarvusDataBox<Transaction>> monthList(MarvusDataWindow bdt)
	{
		return data -> {
			JPanel panel = bdt.getPane();
			int[] columnHeight = new int[12];
			for (int i = 0; i < 12; i++)
				columnHeight[i] = 0;
			SufuTableTools.addRow(bdt, 1, 0, Month.values());
			for (Transaction t : data)
			{
				panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, bdt, evt)), bdt.getGBC(t.getDate().getMonthValue(), ++columnHeight[t.getDate().getMonthValue()]));
			}
		};
	}
	
	public static Consumer<MarvusDataBox<Transaction>> categoryListByMonth(MarvusDataWindow bdt, MarvusDataTableCategorized mdtc)
	{
		return data -> {
			int month = SufuDateUtils.showMonthChooser(bdt).getValue();
			LinkedList<FinancialCategory> list = mdtc.getCategorizedMonth(month);
			JPanel panel = bdt.getPane();
			SufuTableTools.addRow(bdt, 0, 0, "Month", Month.values()[month - 1]);
			SufuTableTools.addRow(bdt, 0, 1, "Name", "Sum");
			month = 2; // Substitute for Y coordinate
			for(FinancialCategory category : list)
			{
				panel.add(SufuFactory.newTextFieldCell(category.getCategory()), bdt.getGBC(0, month));
				panel.add(SufuFactory.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, month++));
				//month++;
			}
		};
	}

	public static Consumer<Collection<Transaction>> categoryYearList(MarvusDataWindow bdt)
	{
		return data -> {
			//LinkedList<FinancialCategory> list = TransactionCalculations.categorizeYearByCategories(data);
			JPanel panel = bdt.getPane();
			SufuTableTools.addRow(bdt, 0, 0, "Category", "Sum");
			int y = 1;
			for(FinancialCategory category : bdt.getDatabase().getCategorizedYearByCategories(2023))
			{
				panel.add(SufuFactory.newTextFieldCell(category.getCategory()), bdt.getGBC(0, y));
				panel.add(SufuFactory.newTextFieldCell(category.getSum().toString()), bdt.getGBC(1, y++));
				//y++;
			}
		};
	}
	
	public static Consumer<MarvusDataBox<Transaction>> categoryByMonthInYear(MarvusDataWindow bdt)
	{
		return data -> bdt.displayData();
	}
	
	public static Consumer<MarvusDataBox<Transaction>> yearOverview(MarvusDataWindow bdt)
	{
		return data -> {
			new YearOverviewTable(bdt, 2023).displayData();;
		};
	}
}
