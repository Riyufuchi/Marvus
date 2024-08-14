package riyufuchi.marvus.tabs;

import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedMonthList extends DataDisplayMode
{
	public CategorizedMonthList(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
	}

	@Override
	public void displayData()
	{
		JPanel pane = targetWindow.getPane();
		int y = 1;
		FinancialCategory fc = null;
		Month[] months = Month.values();
		SufuTableTools.addRowHeader(targetWindow, 0, 0, months);
		Iterator<FinancialCategory> it = null;
		for (int month = 0; month < 12; month++)
		{
			it = dataSource.getCategorizedMonth(months[month]).iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), MarvusGuiUtils.encodeCords(month + 1, y - 1), evt -> btnDataReference(evt)), targetWindow.getGBC(month, y));
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
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, x, y, true));
	}
	
	protected void btnDataReference(ActionEvent e)
	{
		String point = ((JButton)e.getSource()).getName();
		showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
