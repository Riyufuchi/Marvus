package riyufuchi.marvusLib.dataDisplay;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;

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
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		SufuTableTools.addRowHeader(targetWindow, 0, 0, "Category", "Sum");
		int y = 1;
		list = dataSource.getCategorizedYearByCategories(MarvusConfig.financialYear);
		for(FinancialCategory category : list)
		{
			panel.add(SufuFactory.newButton(category.getCategory(), String.valueOf(y), evt -> btnDataReference(evt)), targetWindow.getGBC(0, y));
			panel.add(SufuFactory.newTextFieldHeader(category.getSum().toString()), targetWindow.getGBC(1, y++));
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
	
	//TODO: Create new display mode that display everything in category and sorts it by name
	private void btnDataReference(ActionEvent e)
	{
		//String point = ((JButton)e.getSource()).getName();
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, 
				list.get(Integer.valueOf(((JButton)e.getSource()).getName()) - 1), false));
	}
}
