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
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * This class sort data into categories
 * 
 * @author Riyufuchi
 * @version 1.0
 * @since 0.1.60
 */
public class CategoryYearTable
{
	private ArrayList<LinkedList<FinancialCategory>> months;
	private BudgetDataTable bdt;
	
	public CategoryYearTable(BudgetDataTable bdt)
	{
		months = new ArrayList<>();
		if (bdt == null)
			bdt = new BudgetDataTable();
		this.bdt = bdt;
	}
	
	public void categorize()
	{
		for (int i = 1; i < 13; i++)
			months.add(TransactionCalculations.categorizeMonth(bdt.getDataBox(), i));
	}
	
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
				pane.add(FactoryComponent.newButton(fc.toString(), createBtnName(month, y), evt -> btnDataReference(evt)), bdt.getGBC(month, y));
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

}
