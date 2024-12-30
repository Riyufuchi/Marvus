package riyufuchi.marvus.tabs;

import java.awt.event.ActionEvent;
import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.CategoryDetail;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class CategorizedMonthListTab extends DataDisplayTab
{
	protected final Month[] months;
	protected int y;
	protected FinancialCategory fc;
	protected LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	private String point;
	
	public CategorizedMonthListTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.months = Month.values();
		this.y = 1;
		this.fc = null;
		this.point = "";
		this.categorizedMonths = new LinkedList<>();
		SufuTableTools.addRowHeader(masterPanel, 1, 0, months);
	}

	@Override
	public void displayData()
	{
		for (Month month : months)
		{
			categorizedMonths.add(dataSource.getCategorizedMonth(month));
			createDataTable(categorizedMonths.getLast().iterator(), month);
		}
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, 11);
		categorizedMonths.clear();
		displayData();
	}
	
	protected void createDataTable(Iterator<FinancialCategory> it, Month month)
	{
		while (it.hasNext())
		{
			fc = it.next();
			masterPanel.add(SufuFactory.newButton(fc.toString(), MarvusGuiUtils.encodeCords(month.getValue(), y - 1), evt -> btnDataReference(evt)), masterPanel.getGBC(month.getValue(), y));
			y++;
		}
		y = 1;
	}
	
	protected void showData(int x, int y)
	{
		targetWindow.updateDataDisplayMode(new CategoryDetail(targetWindow, categorizedMonths.get(x - 1).get(y), this));
	}
	
	protected void btnDataReference(ActionEvent e)
	{
		point = ((JButton)e.getSource()).getName();
		showData(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
