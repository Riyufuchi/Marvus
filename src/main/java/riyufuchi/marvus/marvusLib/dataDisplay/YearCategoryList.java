package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;

import riyufuchi.marvus.app.windows.MarvusMainWindow;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.dataStorage.CategoryYearTable;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

public class YearCategoryList extends DataDisplayMode
{
	private LinkedList<FinancialCategory> list;
	
	public YearCategoryList(SufuWindow targetWindow, CategoryYearTable dataSource)
	{
		super(targetWindow, dataSource);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		SufuWindowTools.createTableRowHeader(targetWindow, 1, "Category", "Sum");
		int y = 2;
		list = TransactionCalculations.categorizeYearByCategories(dataSource.getDataBox());
		for(FinancialCategory category : list)
		{
			panel.add(SufuFactory.newButton(category.getCategory(), String.valueOf(y), evt -> btnDataReference(evt)), targetWindow.getGBC(0, y));
			panel.add(SufuFactory.newTextFieldHeader(category.getSum().toString()), targetWindow.getGBC(1, y++));
		}
	}

	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub
	}
	
	private void btnDataReference(ActionEvent e)
	{
		String point = ((JButton)e.getSource()).getName();
		((MarvusMainWindow)targetWindow).showMonthDetailTable(list.get(Integer.valueOf(point) - 2), false);
		//showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
