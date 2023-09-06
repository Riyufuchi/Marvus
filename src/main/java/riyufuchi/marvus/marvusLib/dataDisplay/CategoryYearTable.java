package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
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
import riyufuchi.marvus.marvusLib.financialRecords.YearOverview;
import riyufuchi.marvus.marvusLib.utils.DataBox;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * This class sort data into categories. Data starts from x = 0.
 * 
 * Created On: 24.08.2023<br>
 * Last Edit: 06.09.2023
 * 
 * @author Riyufuchi
 * @version 1.5
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
				x = 0;
				size++;
				return;
			}
		});
		if (x != 0)
		{
			months.get(x).add(new FinancialCategory(transaction));
			size++;
		}
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
	
	public void remove(int month, String name, int id)
	{
		if(name == null || name.isBlank())
			return;
		x = month - 1;
		if (months.get(x).isEmpty())
			return;
		months.get(x).stream().forEach(data -> {
			if (data.getCategory().equals(name))
			{
				data.forEach(t -> {
					if (t.getID() == id)
					{
						dataBox.getList().remove(t);
						data.remove(t);
						size--;
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
		x = transaction.getDate().getMonthValue() - 1;
		if (months.get(x).isEmpty())
			return;
		months.get(x).forEach(data -> {
			if (data.getCategory().equals(transaction.getName()))
			{
				dataBox.getList().remove(transaction);
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
		return size == 0;
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
		size = dataBox.getList().size();
	}
	
	// Money operations
	
	public YearOverview getYearOverview(int year)
	{
		BigDecimal[] income = new BigDecimal[12];
		BigDecimal[] outcome = new BigDecimal[12];
		BigDecimal zero = new BigDecimal(0);
		int index = 0;
		for (int i = 0; i < 12; i++)
		{
			income[i] = new BigDecimal(0);
			outcome[i] = new BigDecimal(0);
		}
		for (Transaction t : dataBox)
		{
			if (t.getDate().getYear() == year)
			{
				index = t.getDate().getMonthValue() - 1;
				switch (t.getValue().compareTo(zero))
				{
					case 1 -> income[index] = income[index].add(t.getValue());
					case -1 -> outcome[index] = outcome[index].add(t.getValue());
					case 0 -> SufuDialogHelper.warningDialog(bdt, "Zero value detected for: "
					+ t.toString() + "\nSome data can be missing", "Zero money sum");
				}
			}
		}
		return new YearOverview(year, income, outcome);
	}
	
	// Utils
	/*
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
	*/
	// Getters
	
	/**
	 * 
	 * @param index month number - 1
	 * @return
	 */
	public LinkedList<FinancialCategory> getCategorizedMonth(int index)
	{
		return months.get(index);
	}
	
	public DataBox<Transaction> getDataBox()
	{
		return dataBox;
	}

}
