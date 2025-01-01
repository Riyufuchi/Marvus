package riyufuchi.marvus.tabs;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.CategoryDetail;
import riyufuchi.marvusLib.dataUtils.FinancialCategory;
import riyufuchi.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

/**
 * @author riyufuchi
 * @since ?
 * @version 01.01.2025
 */
public class YearSummaryTab extends DataDisplayTab
{
	private LinkedList<FinancialCategory> list;
	private BigDecimal outcome;
	private final int YEAR;
	
	public YearSummaryTab(MarvusTabbedFrame targetWindow, int year)
	{
		super(targetWindow);
		this.list = dataSource.getCategorizedYearByCategories(year);
		this.outcome = new BigDecimal(0);
		this.YEAR = year;
		Collections.sort(list, TransactionComparation.compareFC(CompareMethod.By_catagory));
		// UI
		SufuTableTools.addRowHeader(masterPanel, 0, 0, "Category", "Sum");
	}

	@Override
	public void displayData()
	{
		this.outcome = new BigDecimal(0);
		int y = 1;
		BigDecimal bd = null;
		for(FinancialCategory category : list)
		{
			bd = category.getSum();
			masterPanel.add(SufuFactory.newButton(category.getCategory(), String.valueOf(y), evt -> btnDataReference(evt)), masterPanel.getGBC(0, y));
			masterPanel.add(SufuFactory.newTextFieldHeader(bd.toString()), masterPanel.getGBC(1, y++));
			outcome = outcome.add(bd);
		}
		masterPanel.add(SufuFactory.newTextFieldHeader("Outcome"), masterPanel.getGBC(0, y));
		masterPanel.add(SufuFactory.newTextFieldHeader(outcome.toString()), masterPanel.getGBC(1, y));
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 2);
		list = dataSource.getCategorizedYearByCategories(YEAR);
		Collections.sort(list, TransactionComparation.compareFC(CompareMethod.By_catagory));
		displayData();
	}
	
	private void btnDataReference(ActionEvent e)
	{
		targetWindow.updateDataDisplayMode(new CategoryDetail(targetWindow, list.get(Integer.valueOf(((JButton)e.getSource()).getName()) - 1), this));
	}
}
