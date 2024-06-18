package riyufuchi.marvusLib.dataDisplay;

import javax.swing.JPanel;

import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

public class MonthCategoryDetail extends DataDisplayMode
{
	private FinancialCategory fc;
	private int day, numberOfDays;
	
	public MonthCategoryDetail(MarvusDataFrame targetWindow, FinancialCategory fc, boolean dynamicNumberOfDays)
	{
		super(targetWindow);
		initialize(fc, dynamicNumberOfDays);
	}
	
	public MonthCategoryDetail(MarvusDataFrame targetWindow, int x, int y, boolean dynamicNumberOfDays)
	{
		super(targetWindow);
		initialize(dataSource.getCategorizedMonth(x).get(y), dynamicNumberOfDays);
	}
	
	private void initialize(FinancialCategory fc, boolean dynamicNumberOfDays)
	{
		if (fc == null)
			fc = new FinancialCategory(new Transaction());
		if (fc.size() == 0)
			fc.add(new Transaction());
		this.fc = fc;
		this.day = 0;
		if (dynamicNumberOfDays)
			this.numberOfDays = fc.getFirst().getDate().getMonth().length(SufuDateUtils.isLeapYear(fc.getFirst().getDate().getYear()));
		else
			this.numberOfDays = 31;
	}

	@Override
	public void displayData()
	{
		JPanel pane = targetWindow.getPane();
		for (int i = 0; i < numberOfDays; i++)
			pane.add(SufuFactory.newTextFieldHeader((Integer.toString(i + 1))), targetWindow.getGBC(i, 0));
		fillTableData(numberOfDays, pane);
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
	
	private void fillTableData(int numOfDays, JPanel pane)
	{
		int[] yColumn = new int[numOfDays];
		for (int i = 0; i < numOfDays; i++)
			yColumn[i] = 1;
		fc.stream().forEach(data -> {
			day = data.getDate().getDayOfMonth() - 1;
			pane.add(SufuFactory.newTextFieldCell(data.toString(),
					evt -> showExtednedInfo(data, evt)),
					targetWindow.getGBC(day, yColumn[day]));
			yColumn[day] = yColumn[day] + 1;
		});
	}
	
	public FinancialCategory getFinancialCategory()
	{
		return fc;
	}

}
