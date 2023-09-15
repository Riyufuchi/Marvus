package riyufuchi.marvus.marvusLib.legacy;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleOrderableList extends DataDisplayMode
{
	public SimpleOrderableList(MarvusDataFrame targetWindow, TransactionDataTable source)
	{
		super(targetWindow, source);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		int y = 0;
		for (Transaction t : dataSource.getDataBox())
		{
			panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, y++));
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}

}
