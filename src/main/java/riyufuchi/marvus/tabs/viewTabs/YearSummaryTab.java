package riyufuchi.marvus.tabs.viewTabs;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JSpinner;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.CategoryDetail;
import riyufuchi.marvus.tabs.utils.DataDisplayTab;
import riyufuchi.marvusLib.dataUtils.FinancialCategorySafe;
import riyufuchi.marvusLib.dataUtils.MarvusDataComparation;
import riyufuchi.marvusLib.enums.MarvusTransactionOrderBy;
import riyufuchi.sufuLib.gui.utils.SufuFactory;

/**
 * @author riyufuchi
 * @since est. pre. 17.01.2025
 * @version 24.10.2025
 */
public final class YearSummaryTab extends DataDisplayTab
{
	// UI
	private JSpinner columnHeightSpinner;
	// Data
	private LinkedList<FinancialCategorySafe> list;
	private final int YEAR;
	
	public YearSummaryTab(MarvusTabbedFrame targetWindow, int year)
	{
		super(targetWindow);
		this.list = database.getCategorizedYearByCategories(year);
		this.YEAR = year;
		Collections.sort(list, MarvusDataComparation.compareFinancialCategorySafe(MarvusTransactionOrderBy.CATEGORY));
		// UI
		this.columnHeightSpinner = SufuFactory.newSpinner(10, 5, 255, 5, evt -> refresh());
		addMenuAndMenuItems(columnHeightSpinner);
		addContentPanel();
	}

	@Override
	public void displayData()
	{
		int y = 0;
		int x = 0;
		int id = 0;
		for(FinancialCategorySafe category : list)
		{
			contentPanel.add(SufuFactory.newButton(category.getCategory(), String.valueOf(id), evt -> btnDataReference(evt)), contentPanel.getGBC(x, y));
			contentPanel.add(SufuFactory.newTextFieldHeader(category.getSum().toPlainString()), contentPanel.getGBC(x + 1, y));
			y++;
			id++;
			if (y % (int)columnHeightSpinner.getValue() == 0)
			{
				y = 0;
				x += 2;
			}
		}
	}

	@Override
	public void refresh()
	{
		contentPanel.removeAll();
		list = database.getCategorizedYearByCategories(YEAR);
		Collections.sort(list, MarvusDataComparation.compareFinancialCategorySafe(MarvusTransactionOrderBy.CATEGORY));
		displayData();
	}
	
	private void btnDataReference(ActionEvent e)
	{
		targetWindow.updateDataDisplayMode(new CategoryDetail(targetWindow, list.get(Integer.valueOf(((JButton)e.getSource()).getName())), this));
	}
}
