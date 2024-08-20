package riyufuchi.marvusLib.legacy;

import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleOrderableList extends DataDisplayTab
{
	public SimpleOrderableList(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
	}

	@Override
	public void prepareUI()
	{
	}
	
	@Override
	public void displayData()
	{
		int y = 0;
		for (Transaction t : dataSource)
		{
			masterPanel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, y++));
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
