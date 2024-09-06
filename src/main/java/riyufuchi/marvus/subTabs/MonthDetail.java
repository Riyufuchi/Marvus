package riyufuchi.marvus.subTabs;

import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @version 19.08.2024
 */
public class MonthDetail extends DataDisplayTab
{
	private FinancialCategory fc;
	private int day, numberOfDays, x, y;
	
	public MonthDetail(MarvusTabbedFrame targetWindow, FinancialCategory fc, boolean dynamicNumberOfDays, DataDisplayTab parentTab)
	{
		super(targetWindow);
		if (fc == null || fc.size() == 0)
			this.fc = new FinancialCategory(new Transaction());
		else
			this.fc = fc;
		if (dynamicNumberOfDays)
			this.numberOfDays = fc.getFirst().getDate().getMonth().length(SufuDateUtils.isLeapYear(fc.getFirst().getDate().getYear()));
		else
			this.numberOfDays = 31;
		this.day = 0;
		this.x = 0;
		this.y = 0;
		setParentTab(parentTab);
	}
	
	public MonthDetail(MarvusTabbedFrame targetWindow, int x, int y, boolean dynamicNumberOfDays, DataDisplayTab parentTab)
	{
		this(targetWindow, targetWindow.getController().getDatabase().getCategorizedMonth(x).get(y), dynamicNumberOfDays, parentTab);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void prepareUI()
	{
		for (int i = 0; i < numberOfDays; i++)
			masterPanel.add(SufuFactory.newTextFieldHeader((Integer.toString(i + 1))), targetWindow.getGBC(i, 0));
	}
	
	@Override
	public void displayData()
	{
		fillTableData(numberOfDays);
	}

	@Override
	public void refresh()
	{
		clearPanel(masterPanel, numberOfDays - 1);
		if (x != 0)
		{
			fc = dataSource.getCategorizedMonth(x).get(y);
		}
		else
		{
			targetWindow.updateDataDisplayMode(targetWindow.getPreviousTab());
			return;
		}
		displayData();
	}
	
	private void fillTableData(int numOfDays)
	{
		int[] yColumn = new int[numOfDays];
		for (int i = 0; i < numOfDays; i++)
			yColumn[i] = 1;
		fc.stream().forEach(data -> {
			day = data.getDate().getDayOfMonth() - 1;
			masterPanel.add(SufuFactory.newTextFieldCell(data.toString(),
					evt -> showExtednedInfo(data, evt)),
					targetWindow.getGBC(day, yColumn[day]));
			yColumn[day]++;
		});
	}
	
	public FinancialCategory getFinancialCategory()
	{
		return fc;
	}
}
