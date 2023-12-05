package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedMonthList extends DataDisplayMode
{
	public CategorizedMonthList(MarvusDataFrame targetWindow, MarvusDatabase source)
	{
		super(targetWindow, source);
	}

	@Override
	public void displayData()
	{
		JPanel pane = targetWindow.getPane();
		int y = 1;
		FinancialCategory fc = null;
		Month[] months = Month.values();
		SufuTableTools.addRowHeader(targetWindow, 0, 0, months);
		for (int month = 0; month < 12; month++)
		{
			Iterator<FinancialCategory> it = dataSource.getCategorizedMonth(months[month]).iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), createBtnName(month + 1, y), evt -> btnDataReference(evt)), targetWindow.getGBC(month, y));
				y++;
			}
			y = 1;
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
	
	protected void showData(int x, int y)
	{
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, dataSource, x, y, true));
	}
	
	protected final String createBtnName(int month, int y)
	{
		return month + ";" + (y - 1);
	}
	
	protected final void btnDataReference(ActionEvent e)
	{
		String point = ((JButton)e.getSource()).getName();
		showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
