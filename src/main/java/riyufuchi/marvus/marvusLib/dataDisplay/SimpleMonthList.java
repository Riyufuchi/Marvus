package riyufuchi.marvus.marvusLib.dataDisplay;

import java.time.Month;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class SimpleMonthList extends DataDisplayMode
{
	public SimpleMonthList(MarvusDataFrame targetWindow, MarvusDatabase source)
	{
		super(targetWindow, source);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		int[] columnHeight = new int[12];
		for (int i = 0; i < 12; i++)
			columnHeight[i] = 0;
		SufuTableTools.addRowHeader(targetWindow, 1, 0, Month.values());
		for (Transaction t : dataSource)
		{
			panel.add(SufuFactory.newTextFieldCell(t.toString(),
					evt -> showExtednedInfo(t, evt)),
					targetWindow.getGBC(t.getDate().getMonthValue(), ++columnHeight[t.getDate().getMonthValue()]));
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
