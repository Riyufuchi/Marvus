package riyufuchi.marvus.marvusLib.dataDisplay;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleList extends DataDisplayMode
{
	public SimpleList(SufuWindow targetWindow, TransactionDataTable source)
	{
		super(targetWindow, source);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		dataSource.getDataBox().stream().forEach(t -> panel.add(SufuFactory.newTextFieldCell(t.toString(),
				evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, t.getID())));
		//targetWindow.refreshWindow();
	}

	@Override
	public void refresh()
	{
		JPanel panel = targetWindow.getPane();
		panel.removeAll();
		dataSource.getDataBox().stream().forEach(t -> panel.add(SufuFactory.newTextFieldCell(t.toString(),
				evt -> showExtednedInfo(t, evt)),
				targetWindow.getGBC(0, t.getID())));
		//targetWindow.refreshWindow();
	}
}
