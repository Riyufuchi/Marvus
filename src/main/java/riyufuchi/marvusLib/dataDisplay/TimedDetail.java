package riyufuchi.marvusLib.dataDisplay;

import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @since 18.06.2024
 * @version 18.06.2024
 */
public class TimedDetail extends DataDisplayMode
{
	private LocalDateTime fromDate, toDate;
	private JButton dateFrom, dateTo;
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;

	public TimedDetail(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		this.toDate = SufuDateUtils.toLocalDateTime(SufuDateUtils.nowDateString());
		this.fromDate = LocalDateTime.now().minusDays(4);
	}

	@Override
	public void displayData()
	{
		JPanel pane = targetWindow.getPane();
		JPanel flowPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		dateFrom = SufuFactory.newButton("", evt -> {
			fromDate = new SufuDatePicker(targetWindow.getSelf(), fromDate).showAndGet();
			MarvusUtils.editDateText(dateFrom, fromDate);
		}); 
		dateTo = SufuFactory.newButton("", evt -> {
			toDate = new SufuDatePicker(targetWindow.getSelf(), toDate).showAndGet();
			MarvusUtils.editDateText(dateTo, toDate);
		}); 
		
		MarvusUtils.editDateText(dateFrom, fromDate);
		MarvusUtils.editDateText(dateTo, toDate);
		
		flowPane.add(dateFrom);
		flowPane.add(SufuFactory.newTextFieldCell("to"));
		flowPane.add(dateTo);
		
		pane.add(flowPane, targetWindow.getGBC(0, 0));
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
	
	private void prepData()
	{
		for (int x = fromDate.getMonthValue(); x < toDate.getMonthValue(); x++)
		{
			//TODO: Load and sort data in given dates
		}
	}
}
