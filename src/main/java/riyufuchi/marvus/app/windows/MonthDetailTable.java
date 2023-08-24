package riyufuchi.marvus.app.windows;

import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.DataDisplay;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;

/**
 * This window shows transaction per day in given month<br><br>
 * 
 * Created On: 21.08.2023<br>
 * Last Edit: 24.08.2023
 * 
 * @author Riyufuchi
 */
public class MonthDetailTable extends SufuWindow
{
	private FinancialCategory fc;
	private int day;
	private BudgetDataTable bdt;
	
	public MonthDetailTable(FinancialCategory fc, BudgetDataTable bdt)
	{
		super("Month", 800, 600, false, true, false);
		if (fc == null)
			fc = new FinancialCategory(new Transaction());
		if (fc.size() == 0)
			fc.add(new Transaction());
		this.fc = fc;
		this.bdt = bdt;
		this.day = 0;
		setTitle(fc.getFirst().getName() + " - " + fc.getFirst().getDate().getMonth().name() + " - " + fc.getFirst().getDate().getYear());
		createTableHeader();
	}

	@Override
	protected void setComponents(JPanel panel)
	{
	}

	private boolean isLeapYear(int year)
	{
		return ((year % 4 == 0 && year % 100!= 0) || year % 400 == 0);
	}
	
	private void createTableHeader()
	{
		int numberOfDays = fc.getFirst().getDate().getMonth().length(isLeapYear(fc.getFirst().getDate().getYear()));
		JPanel pane = getPane();
		for (int i = 0; i < numberOfDays; i++)
			pane.add(FactoryComponent.newTextFieldCell((Integer.toString(i + 1))), getGBC(i, 0));
		fillTableData(numberOfDays, pane);
	}
	
	private void fillTableData(int numOfDays, JPanel pane)
	{
		int[] yColumn = new int[numOfDays];
		for (int i = 0; i < numOfDays; i++)
			yColumn[i] = 1;
		fc.stream().forEach(data -> {
			day = data.getDate().getDayOfMonth() - 1;
			pane.add(FactoryComponent.newTextFieldCell(data.toString(), evt -> DataDisplay.showExtednedInfo(data, bdt, evt)),
					getGBC(day, yColumn[day]));
			yColumn[day] = yColumn[day] + 1;
		});
	}
}
