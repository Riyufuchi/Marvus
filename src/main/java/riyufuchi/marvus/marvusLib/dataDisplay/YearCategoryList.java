package riyufuchi.marvus.marvusLib.dataDisplay;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

public class YearCategoryList extends DataDisplayMode
{
	public YearCategoryList(SufuWindow targetWindow, CategoryYearTable dataSource)
	{
		super(targetWindow, dataSource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		SufuWindowTools.createTableRow(targetWindow, 1, "Category", "Sum");
		int y = 2;
		for(FinancialCategory category : TransactionCalculations.categorizeYearByCategories(dataSource.getDataBox()))
		{
			panel.add(SufuFactory.newTextFieldCell(category.getCategory()), targetWindow.getGBC(0, y));
			panel.add(SufuFactory.newTextFieldCell(category.getSum().toString()), targetWindow.getGBC(1, y++));
		}
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

}
