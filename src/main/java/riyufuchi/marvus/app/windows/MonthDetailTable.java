package riyufuchi.marvus.app.windows;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.MonthCategoryDetail;
import riyufuchi.marvus.marvusLib.legacy.DataDisplay;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

/**
 * This window shows transaction per day in given month<br><br>
 * 
 * Created On: 21.08.2023<br>
 * Last Edit: 06.09.2023
 * 
 * @author Riyufuchi
 */
public class MonthDetailTable extends SufuWindow
{
	public MonthDetailTable(BudgetDataTable bdt, FinancialCategory fc)
	{
		super("Month", 800, 600, false, true, false);
		setTitle(createWindowTitle(fc.getFirst()));
		new MonthCategoryDetail(this, fc).displayData();
		refreshWindow();
	}

	@Override
	protected void setComponents(JPanel panel)
	{
	}
	
	private String createWindowTitle(Transaction t)
	{
		return t.getName() + " - " + t.getDate().getMonth().name() + " - " + t.getDate().getYear();
	}
}
