package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

public class CategorizedMonthList extends DataDisplayMode
{
	public CategorizedMonthList(SufuWindow targetWindow, TransactionDataTable source)
	{
		super(targetWindow, source);
	}

	@Override
	public void displayData()
	{
		JPanel pane = targetWindow.getPane();
		int y = 1;
		FinancialCategory fc = null;
		SufuWindowTools.createTableRowHeader(targetWindow, 0, Month.values());
		for (int month = 0; month < 12; month++)
		{
			Iterator<FinancialCategory> it = dataSource.getCategorizedMonth(month).iterator();
			while (it.hasNext())
			{
				fc = it.next();
				pane.add(SufuFactory.newButton(fc.toString(), createBtnName(month, y), evt -> btnDataReference(evt)), targetWindow.getGBC(month, y));
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
	
	private void showData(int x, int y)
	{
		((MarvusDataWindow)targetWindow).showMonthDetailTable(dataSource.get(x, y), true);
	}
	
	private String createBtnName(int month, int y)
	{
		return month + ";" + (y - 1);
	}
	
	private void btnDataReference(ActionEvent e)
	{
		String point = ((JButton)e.getSource()).getName();
		showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
