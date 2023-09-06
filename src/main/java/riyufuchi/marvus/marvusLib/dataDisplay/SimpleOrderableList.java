package riyufuchi.marvus.marvusLib.dataDisplay;

import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.app.windows.BudgetDataTable;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

public class SimpleOrderableList extends DataDisplayMode {

	public SimpleOrderableList(SufuWindow targetWindow, CategoryYearTable source) {
		super(targetWindow, source);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayData()
	{
		JPanel panel = targetWindow.getPane();
		int y = 0;
		for (Transaction t : dataSource.getDataBox())
		{
			panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> DataDisplay.showExtednedInfo(t, (BudgetDataTable)targetWindow, evt)), targetWindow.getGBC(0, y++));
		}
	}

	@Override
	public void refresh()
	{
		JPanel panel = targetWindow.getPane();
		int y = 0;
		for (Transaction t : dataSource.getDataBox())
		{
			((JTextField)panel.getComponentAt(0, y++)).setText(t.toString());
			//panel.add(SufuFactory.newTextFieldCell(t.toString(), evt -> DataDisplay.showExtednedInfo(t, (BudgetDataTable)targetWindow, evt)), targetWindow.getGBC(0, y++));
		}
	}

}
