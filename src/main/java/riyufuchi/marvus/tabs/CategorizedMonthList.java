package riyufuchi.marvus.tabs;

import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.Iterator;

import javax.swing.JButton;

import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedMonthList extends DataDisplayMode
{
	protected final Month[] months;
	protected int y;
	protected FinancialCategory fc;
	private String point;
	
	public CategorizedMonthList(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		this.months = Month.values();
		this.totalItems = 11;
		this.y = 1;
		this.fc = null;
		this.point = "";
	}
	

	@Override
	public void prepareUI()
	{
		SufuTableTools.addRowHeader(targetWindow, 1, 0, months);
	}

	@Override
	public void displayData()
	{
		for (Month month : months)
		{
			createDataTable(dataSource.getCategorizedMonth(month).iterator(), month);
		}
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 11);
		totalItems = 11;
		displayData();
	}
	
	protected void createDataTable(Iterator<FinancialCategory> it, Month month)
	{
		while (it.hasNext())
		{
			fc = it.next();
			masterPanel.add(SufuFactory.newButton(fc.toString(), MarvusGuiUtils.encodeCords(month.getValue(), y - 1), evt -> btnDataReference(evt)), targetWindow.getGBC(month.getValue(), y));
			y++;
		}
		totalItems += --y;
		y = 1;
	}
	
	protected void showData(int x, int y)
	{
		targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, x, y, true));
	}
	
	protected void btnDataReference(ActionEvent e)
	{
		point = ((JButton)e.getSource()).getName();
		showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
