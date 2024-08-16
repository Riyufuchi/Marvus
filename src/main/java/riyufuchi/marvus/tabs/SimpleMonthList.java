package riyufuchi.marvus.tabs;

import java.time.Month;

import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class SimpleMonthList extends DataDisplayMode
{
	private int[] columnHeight;
	private final int NUM_OF_COLUMNS;
	
	public SimpleMonthList(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		this.NUM_OF_COLUMNS = 13; // For optimization purpose
		this.columnHeight = new int[NUM_OF_COLUMNS];
	}
	
	@Override
	public void prepareUI()
	{
		SufuTableTools.addRowHeader(targetWindow, 1, 0, Month.values()); // x = 1 because month values are 1 - 12
	}

	@Override
	public void displayData()
	{
		for (int i = 1; i < NUM_OF_COLUMNS; i++)
			columnHeight[i] = 0;
		for (Transaction t : dataSource)
		{
			masterPanel.add(SufuFactory.newTextFieldCell(t.toString(),
					evt -> showExtednedInfo(t, evt)),
					targetWindow.getGBC(t.getDate().getMonthValue(), ++columnHeight[t.getDate().getMonthValue()]));
		}
	}

	@Override
	public void refresh()
	{
		totalItems = 11; // Because first 11 components are table header
		for (int i = 1; i < NUM_OF_COLUMNS; i++)
			totalItems += columnHeight[i];
		clearPanel(masterPanel, 11);
		displayData();
	}
}
