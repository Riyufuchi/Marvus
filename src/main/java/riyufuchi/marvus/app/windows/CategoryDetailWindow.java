package riyufuchi.marvus.app.windows;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.MonthCategoryDetail;
import riyufuchi.sufuLib.gui.SufuWindow;

/**
 * This window shows transaction per day in given month<br><br>
 * 
 * Created On: 21.08.2023<br>
 * Last Edit: 07.09.2023
 * 
 * @author Riyufuchi
 */
public class CategoryDetailWindow extends SufuWindow
{
	private MonthCategoryDetail mcd;
	
	public CategoryDetailWindow(MarvusMainWindow bdt, FinancialCategory fc, boolean dynamic)
	{
		super("?", 800, 600, false, true, false);
		this.mcd = new MonthCategoryDetail(this, fc);
		if (dynamic)
			setTitle(createWindowTitle(mcd.getFinancialCategory().getFirst()));
		else
		{
			setTitle(fc.getFirst().getCategory() + " - " + fc.getFirst().getDate().getYear());
			mcd.setNumberOfDays(31);
		}
		mcd.displayData();
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
