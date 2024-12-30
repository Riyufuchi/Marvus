package riyufuchi.marvus.tabs.subTabs;

import javax.swing.JPanel;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.FinancialCategory;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGridPane;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @version 08.11.2024
 */
@Deprecated
public class MonthDetail extends DataDisplayTab
{
	private FinancialCategory fc;
	private SufuGridPane pane;
	private JPanel menu;
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
		this.menu = SufuFactory.newFlowPane();
		this.pane = new SufuGridPane();
		masterPanel.add(menu, targetWindow.getGBC(0, 0));
		masterPanel.add(pane, targetWindow.getGBC(0, 1));
		for (int i = 0; i < numberOfDays; i++)
			pane.add(SufuFactory.newTextFieldHeader((Integer.toString(i + 1))), targetWindow.getGBC(i, 0));
		menu.add(SufuFactory.newButton("View as table", evt -> targetWindow.updateDataDisplayMode(new TableDetail(targetWindow, fc, this)))); 
		//new TransactionViewer(targetWindow.getSelf(), fc).showDialog()));
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
			targetWindow.updateDataDisplayMode(getSuperTab());
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
			pane.add(SufuFactory.newTextFieldCell(data.toString(),
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
