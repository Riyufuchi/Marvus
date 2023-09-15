package riyufuchi.marvus.app.windows.legacy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataDisplay.MonthCategoryDetail;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.gui.SufuWindow;

/**
 * This window shows transactions per day in given month.<br><br>
 * NOTE: This window was causing problems when transaction was edited or deleted.<br><br>
 * 
 * Created On: 21.08.2023<br>
 * Last Edit: 15.09.2023
 * 
 * @author Riyufuchi
 */
@Deprecated
public class CategoryDetailWindow extends SufuWindow implements MarvusDataFrame
{
	private DataDisplayMode mcd;
	
	public CategoryDetailWindow(MarvusDataWindow bdt, FinancialCategory fc, boolean dynamic)
	{
		super("?", 800, 600, false, true, false);
		this.mcd = new MonthCategoryDetail(this, fc, dynamic);
		if (dynamic)
			setTitle(createWindowTitle(fc.getFirst()));
		else
		{
			setTitle(fc.getFirst().getCategory() + " - " + fc.getFirst().getDate().getYear());
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

	@Override
	public void updateDataDisplayMode(DataDisplayMode dataDisplayMode)
	{
		if (dataDisplayMode == null)
			return;
		mcd = dataDisplayMode;
		mcd.displayData();
		
	}

	@Override
	public JFrame getSelf()
	{
		return this;
	}
}
