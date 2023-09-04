package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;

import riyufuchi.marvus.app.windows.BudgetDataTable;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.utils.DataBox;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * This class sort data into categories. Data starts from x = 0.
 * 
 * Created On: 24.08.2023<br>
 * Last Edit: 04.09.2023
 * 
 * @author Riyufuchi
 * @version 1.1
 * @since 0.1.60
 */
public class CategoryYearTable
{
	private ArrayList<LinkedList<FinancialCategory>> months;
	private DataBox<Transaction> dataBox;
	private BudgetDataTable bdt;
	private int x, size;
	
	public CategoryYearTable(BudgetDataTable bdt)
	{
		initialize();
		this.x = 0;
		//if (bdt == null)
			//bdt = new BudgetDataTable();
		this.bdt = bdt;
		this.dataBox = new DataBox<>(e -> SufuDialogHelper.exceptionDialog(bdt, e),
				TransactionComparation.compareBy(CompareMethod.OldestToNewest));
	}
	
	private void initialize()
	{
		this.months = new ArrayList<>();
		for (int i = 0; i < 12; i++)
			months.add(new LinkedList<>());
		this.size = 0;
	}
	
	// Data structure methods
	
	public void add(Transaction transaction)
	{
		if (transaction == null)
			return;
		dataBox.add(transaction);
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
		{
			months.get(x).add(new FinancialCategory(transaction));
			size++;
			return;
		}
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				data.add(transaction);
				transaction.setName("");
				size++;
				return;
			}
		});
		if (transaction.getName().equals(""))
			months.get(x).add(new FinancialCategory(transaction));
		size++;
	}
	
	public void set(Transaction transaction)
	{
		if (transaction == null)
			return;
		x = transaction.getDate().getMonthValue() - 1;
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				data.forEach(transactionOld -> {
					if (transactionOld.getID() == transaction.getID())
					{
						transactionOld = transaction;
						return;
					}
					
				});
				return;
			}
		});
	}
	
	public void remove(Transaction transaction)
	{
		if (transaction == null)
			return;
		dataBox.getList().remove();
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
			return;
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				data.remove(transaction);
				return;
			}
		});
		size--;
	}
	
	public FinancialCategory get(int x, int y)
	{
		return months.get(x).get(y);
	}
	
	public boolean isEmpty()
	{
		return size > 0;
	}
	
	public void clear()
	{
		initialize();
	}
	
	public int size()
	{
		return size;
	}
	
	public void rebuild()
	{
		clear();
		for (int i = 1; i < 13; i++)
			months.set(i - 1, TransactionCalculations.categorizeMonth(dataBox, i));
	}
	
	// Utils
	
	public void displayData()
	{
		JPanel pane = bdt.getPane();
		int y = 1;
		FinancialCategory fc = null;
		SufuWindowTools.createTableRow(bdt, 0, 0, (Object[])Month.values());
		for (int month = 0; month < 12; month++)
		{
			Iterator<FinancialCategory> it = months.get(month).iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), createBtnName(month, y), evt -> btnDataReference(evt)), bdt.getGBC(month, y));
				y++;
			}
			y = 1;
		}
	}

	private void showData(int x, int y)
	{
		bdt.showMonthDetailTable(months.get(x).get(y));
	}
	
	private String createBtnName(int month, int y)
	{
		return month + ";" + (y - 1);
	}
	
	private void btnDataReference(ActionEvent e)
	{
		String point = ((JButton)e.getSource()).getName();
		showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
	
	// Getters
	
	public DataBox<Transaction> getDataBox()
	{
		return dataBox;
	}

}