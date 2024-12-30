package riyufuchi.marvusLib.legacy;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleList extends DataDisplayTab
{
	public SimpleList(MarvusTabbedFrame targetWindow)
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
		dataSource.stream().forEach(t -> masterPanel.add(SufuFactory.newTextFieldCell(t.toString(),
				evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, t.getID())));
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
