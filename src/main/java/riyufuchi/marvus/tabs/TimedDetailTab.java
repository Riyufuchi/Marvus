package riyufuchi.marvus.tabs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import riyufuchi.marvus.subTabs.MonthDetail;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @since 18.06.2024
 * @version 20.08.2024
 */
public class TimedDetailTab extends DataDisplayTab
{
	private LocalDateTime fromDate, toDate;
	private JButton dateFrom, dateTo;
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	private JPanel dataPane, pane, flowPane;
	private Point p;

	public TimedDetailTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.toDate = SufuDateUtils.toLocalDateTime(SufuDateUtils.nowDateString());
		this.fromDate = LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay(); // So we get data for past 7 days by default
		this.categorizedMonths = new LinkedList<>();
	}
	
	@Override
	public void prepareUI()
	{
		pane = targetWindow.getPane();
		flowPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		dataPane = new JPanel(new GridLayout(0, 2));
		
		dateFrom = SufuFactory.newButton("", evt -> {
			fromDate = new SufuDatePicker(targetWindow.getSelf(), fromDate).showAndGet();
			MarvusGuiUtils.editDateText(dateFrom, fromDate);
			refresh();
		}); 
		dateTo = SufuFactory.newButton("", evt -> {
			toDate = new SufuDatePicker(targetWindow.getSelf(), toDate).showAndGet();
			MarvusGuiUtils.editDateText(dateTo, toDate);
			refresh();
		}); 
		
		MarvusGuiUtils.editDateText(dateFrom, fromDate);
		MarvusGuiUtils.editDateText(dateTo, toDate);
		
		flowPane.add(dateFrom);
		flowPane.add(new JLabel("to"));
		flowPane.add(dateTo);
		
		pane.add(flowPane, targetWindow.getGBC(0, 0));
		pane.add(dataPane, targetWindow.getGBC(0, 1));
		prepData();
	}

	@Override
	public void displayData()
	{
		printData();
	}

	@Override
	public void refresh()
	{
		dataPane.removeAll();
		prepData();
		printData();
	}
	
	private void printData()
	{
		BigDecimal income = new BigDecimal(0);
		BigDecimal spendings = new BigDecimal(0);
		BigDecimal zero = new BigDecimal(0);
		BigDecimal holder = null;
		int x = 0;
		int y = 0;
		for (LinkedList<FinancialCategory> data : categorizedMonths)
		{
			for (FinancialCategory cat : data)
			{
				holder = cat.getSum();
				dataPane.add(SufuFactory.newButton(cat.getCategory(), MarvusGuiUtils.encodeCords(x, y), evt -> {
					p = MarvusGuiUtils.extractPointFromButtonName(evt);
					targetWindow.updateDataDisplayMode(new MonthDetail(targetWindow, categorizedMonths.get(p.x).get(p.y), false, this));
				}));
				dataPane.add(SufuFactory.newTextFieldHeader(holder.toString()));
				if (holder.compareTo(zero) > 0)
					income = income.add(holder);
				else
					spendings = spendings.add(holder);
				y++;
			}
			x++;
			y = 0;
		}
		dataPane.add(SufuFactory.newTextFieldHeader("Income:"));
		dataPane.add(SufuFactory.newTextFieldHeader(income.toString()));
		dataPane.add(SufuFactory.newTextFieldHeader("Spendings:"));
		dataPane.add(SufuFactory.newTextFieldHeader(spendings.toString()));
		dataPane.add(SufuFactory.newTextFieldHeader("Outcome:"));
		dataPane.add(SufuFactory.newTextFieldHeader(income.add(spendings).toString()));
	}
	
	private void prepData()
	{
		categorizedMonths.clear();
		for (int x = fromDate.getMonthValue(); x <= toDate.getMonthValue(); x++)
		{
			categorizedMonths.add(dataSource.getCategorizedMonthByNames(x));
		}
		Iterator<Transaction> it;
		Iterator<FinancialCategory> it_fc;
		Transaction t = null;
		FinancialCategory cat = null;
		for (LinkedList<FinancialCategory> data : categorizedMonths)
		{
			it_fc = data.iterator();
			while (it_fc.hasNext())
			{
				cat = it_fc.next();
				it = cat.iterator();
				while (it.hasNext())
				{
					t = it.next();
					if (!((t.getDate().isAfter(fromDate) || t.getDate().equals(fromDate)) && (t.getDate().isBefore(toDate) || t.getDate().equals(toDate))))
						it.remove();
				}
				if (cat.getSum().intValue() == 0)
					it_fc.remove();
			}
		}
	}
}
