package riyufuchi.marvusLib.legacy;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleOrderableList extends DataDisplayTab
{
	public SimpleOrderableList(MarvusTabbedFrame targetWindow)
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
