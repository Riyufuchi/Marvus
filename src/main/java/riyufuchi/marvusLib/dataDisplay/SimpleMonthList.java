package riyufuchi.marvusLib.dataDisplay;

import java.time.Month;

import javax.swing.JPanel;

import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class SimpleMonthList extends DataDisplayMode
{
	public SimpleMonthList(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
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
					targetWindow.getGBC(t.getDate().getMonthValue(), ++columnHeight[t.getDate().getMonthValue() - 1]));
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
