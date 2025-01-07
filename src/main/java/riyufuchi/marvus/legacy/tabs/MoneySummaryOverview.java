package riyufuchi.marvus.legacy.tabs;

import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.legacy.data.MoneySum;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.marvusLib.dataStorage.MarvusDataBox;
import riyufuchi.sufuLib.gui.utils.SufuFactory;

public class MoneySummaryOverview extends DataDisplayTab
{
	private JTextField[][] textFields;
	private MarvusDataBox<MoneySum> data;
	
	public MoneySummaryOverview(MarvusTabbedFrame targetWindow, MarvusDataBox<MoneySum> data)
	{
		super(targetWindow, null);
		this.data = data;
	}

	@Override
	public void displayData()
	{
		if (data.isEmpty())
			return;
		textFields = new JTextField[data.size()][2];
		String[] listData = data.getList().get(0).toString().split(";");
		char oldDate = listData[1].charAt(listData[1].length()-1);
		Iterator<MoneySum> it = data.iterator();
		int year = 0;
		JPanel pane = targetWindow.getPane();
		int y = 0;
		for(int x = 0; x < textFields.length; x++)
		{
			listData = it.next().getDataArray();
			if(oldDate != listData[1].charAt(listData[1].length() - 1))
			{
				year = year + 2;
				y = 0;
				oldDate = listData[1].charAt(listData[1].length() - 1);
			}
			for(int i = 0; i < textFields[0].length; i++)
			{
				textFields[x][i] = SufuFactory.newTextFieldCell(listData[i]);
				pane.add(textFields[x][i], targetWindow.getGBC(i + year, y + 1));
			}
			y++;
		}
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}

	@Override
	public void prepareUI()
	{
	}
}
