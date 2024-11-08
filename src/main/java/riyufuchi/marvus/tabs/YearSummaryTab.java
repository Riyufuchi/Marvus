package riyufuchi.marvus.tabs;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.tabs.subTabs.CategoryDetail;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

// TODO: Add over spending guard
public class YearSummaryTab extends DataDisplayTab
{
	private LinkedList<FinancialCategory> list;
	private BigDecimal outcome;
	
	public YearSummaryTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.list = dataSource.getCategorizedYearByCategories(MarvusConfig.currentFinancialYear);
		this.outcome = new BigDecimal(0);
		Collections.sort(list, TransactionComparation.compareFC(CompareMethod.By_catagory));
	}
	
	@Override
	public void prepareUI()
	{
		SufuTableTools.addRowHeader(targetWindow, 0, 0, "Category", "Sum");
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
			masterPanel.add(SufuFactory.newButton(category.getCategory(), String.valueOf(y), evt -> btnDataReference(evt)), targetWindow.getGBC(0, y));
			masterPanel.add(SufuFactory.newTextFieldHeader(bd.toString()), targetWindow.getGBC(1, y++));
			outcome = outcome.add(bd);
		}
		masterPanel.add(SufuFactory.newTextFieldHeader("Outcome"), targetWindow.getGBC(0, y));
		masterPanel.add(SufuFactory.newTextFieldHeader(outcome.toString()), targetWindow.getGBC(1, y));
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 2);
		list = dataSource.getCategorizedYearByCategories(MarvusConfig.currentFinancialYear);
		Collections.sort(list, TransactionComparation.compareFC(CompareMethod.By_catagory));
		displayData();
	}
	
	private void btnDataReference(ActionEvent e)
	{
		targetWindow.updateDataDisplayMode(new CategoryDetail(targetWindow, list.get(Integer.valueOf(((JButton)e.getSource()).getName()) - 1), this));
	}
}
