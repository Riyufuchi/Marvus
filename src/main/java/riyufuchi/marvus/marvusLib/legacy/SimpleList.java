package riyufuchi.marvus.marvusLib.legacy;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataBase.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleList extends DataDisplayMode
{
	public SimpleList(MarvusDataFrame targetWindow, MarvusDatabase source)
	{
		super(targetWindow, source);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		dataSource.stream().forEach(t -> panel.add(SufuFactory.newTextFieldCell(t.toString(),
				evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, t.getID())));
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
}
