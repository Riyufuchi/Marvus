package riyufuchi.marvus.marvusLib.dataDisplay;

import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

public class MonthCategoryDetail extends DataDisplayMode
{
	private FinancialCategory fc;
	private int day, numberOfDays;
	
	public MonthCategoryDetail(SufuWindow targetWindow, FinancialCategory fc)
	{
		super(targetWindow, null);
		initialize(fc);
		this.numberOfDays =  fc.getFirst().getDate().getMonth().length(SufuDateUtils.isLeapYear(fc.getFirst().getDate().getYear()));
	}
	
	private void initialize(FinancialCategory fc)
	{
		if (fc == null)
			fc = new FinancialCategory(new Transaction());
		if (fc.size() == 0)
			fc.add(new Transaction());
		this.fc = fc;
		this.day = 0;
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
	public void refresh() {
		// TODO Auto-generated method stub

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
	
	public void setNumberOfDays(int num)
	{
		this.numberOfDays = num;
	}
	
	public FinancialCategory getFinancialCategory()
	{
		return fc;
	}

}
