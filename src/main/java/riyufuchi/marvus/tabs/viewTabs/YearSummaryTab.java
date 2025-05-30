package riyufuchi.marvus.tabs.viewTabs;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.CategoryDetail;
import riyufuchi.marvus.tabs.utils.DataDisplayTab;
import riyufuchi.marvusLib.dataUtils.FinancialCategorySafe;
import riyufuchi.marvusLib.dataUtils.MarvusDataComparation;
import riyufuchi.marvusLib.enums.MarvusTransactionOrderBy;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuTableTools;

/**
 * @author riyufuchi
 * @since ?
 * @version 17.01.2025
 */
public class YearSummaryTab extends DataDisplayTab
{
	private LinkedList<FinancialCategorySafe> list;
	private BigDecimal outcome;
	private final int YEAR;
	
	public YearSummaryTab(MarvusTabbedFrame targetWindow, int year)
	{
		super(targetWindow);
		this.list = database.getCategorizedYearByCategories(year);
		this.outcome = new BigDecimal(0);
		this.YEAR = year;
		Collections.sort(list, MarvusDataComparation.compareFinancialCategorySafe(MarvusTransactionOrderBy.CATEGORY));
		// UI
		SufuTableTools.addRowHeader(masterPanel, 0, 0, "Category", "Sum");
	}

	@Override
	public void displayData()
	{
		this.outcome = new BigDecimal(0);
		int y = 1;
		BigDecimal bd = null;
		for(FinancialCategorySafe category : list)
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
		list = database.getCategorizedYearByCategories(YEAR);
		Collections.sort(list, MarvusDataComparation.compareFinancialCategorySafe(MarvusTransactionOrderBy.CATEGORY));
		displayData();
	}
	
	private void btnDataReference(ActionEvent e)
	{
		targetWindow.updateDataDisplayMode(new CategoryDetail(targetWindow, list.get(Integer.valueOf(((JButton)e.getSource()).getName()) - 1), this));
	}
}
