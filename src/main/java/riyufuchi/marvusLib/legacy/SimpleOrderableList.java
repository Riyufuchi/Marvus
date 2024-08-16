package riyufuchi.marvusLib.legacy;

import javax.swing.JPanel;

import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleOrderableList extends DataDisplayMode
{
	public SimpleOrderableList(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		int y = 0;
		for (Transaction t : dataSource)
		{
			panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> showExtednedInfo(t, evt)), targetWindow.getGBC(0, y++));
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}

	@Override
	public void prepareUI() {
		// TODO Auto-generated method stub
		
	}

}
