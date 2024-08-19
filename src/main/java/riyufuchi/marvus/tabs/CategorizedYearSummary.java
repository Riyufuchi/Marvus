package riyufuchi.marvus.tabs;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.subTabs.MonthDetail;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedYearSummary extends DataDisplayMode
{
	private LinkedList<FinancialCategory> list;
	
	public CategorizedYearSummary(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		this.list = dataSource.getCategorizedYearByCategories(MarvusConfig.financialYear);
	}
	
	@Override
	public void prepareUI()
	{
		SufuTableTools.addRowHeader(targetWindow, 0, 0, "Category", "Sum");	
	}

	@Override
	public void displayData()
	{
		int y = 1;
		
		for(FinancialCategory category : list)
		{
			masterPanel.add(SufuFactory.newButton(category.getCategory(), String.valueOf(y), evt -> btnDataReference(evt)), targetWindow.getGBC(0, y));
			masterPanel.add(SufuFactory.newTextFieldHeader(category.getSum().toString()), targetWindow.getGBC(1, y++));
		}
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 2);
		list = dataSource.getCategorizedYearByCategories(MarvusConfig.financialYear);
		displayData();
	}
	
	//TODO: Create new display mode that display everything in category and sorts it by name
	private void btnDataReference(ActionEvent e)
	{
		targetWindow.updateDataDisplayMode(new MonthDetail(targetWindow, 
				list.get(Integer.valueOf(((JButton)e.getSource()).getName()) - 1), false));
	}
}
