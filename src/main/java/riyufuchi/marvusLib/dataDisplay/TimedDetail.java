package riyufuchi.marvusLib.dataDisplay;

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

import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @since 18.06.2024
 * @version 20.06.2024
 */
public class TimedDetail extends DataDisplayMode
{
	private LocalDateTime fromDate, toDate;
	private JButton dateFrom, dateTo;
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	private JPanel dataPane, pane, flowPane;
	private Point p;

	public TimedDetail(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		this.toDate = SufuDateUtils.toLocalDateTime(SufuDateUtils.nowDateString());
		this.fromDate = LocalDateTime.now().minusDays(4);
		this.fromDate = fromDate.toLocalDate().atStartOfDay();
		this.categorizedMonths = new LinkedList<>();
	}

	@Override
	public void displayData()
	{
		createUI();
		dataPane.removeAll();
		categorizedMonths.clear();
		prepData();
		printData();
		dataPane.revalidate();
		dataPane.repaint();
		pane.revalidate();
		pane.repaint();
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
	
	private void createUI()
	{
		pane = targetWindow.getPane();
		flowPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		dataPane = new JPanel(new GridLayout(0, 2));
		
		dateFrom = SufuFactory.newButton("", evt -> {
			fromDate = new SufuDatePicker(targetWindow.getSelf(), fromDate).showAndGet();
			MarvusUtils.editDateText(dateFrom, fromDate);
			refresh();
		}); 
		dateTo = SufuFactory.newButton("", evt -> {
			toDate = new SufuDatePicker(targetWindow.getSelf(), toDate).showAndGet();
			MarvusUtils.editDateText(dateTo, toDate);
			refresh();
		}); 
		
		MarvusUtils.editDateText(dateFrom, fromDate);
		MarvusUtils.editDateText(dateTo, toDate);
		
		flowPane.add(dateFrom);
		flowPane.add(new JLabel("to"));
		flowPane.add(dateTo);
		
		pane.add(flowPane, targetWindow.getGBC(0, 0));
		pane.add(dataPane, targetWindow.getGBC(0, 1));
	}
	
	private void printData()
	{
		BigDecimal income = new BigDecimal(0);
		BigDecimal spendings = new BigDecimal(0);
		BigDecimal zero = new BigDecimal(0);
		BigDecimal holder = null;
		int x = 0;
		int y = 1; // Because createBtnName(x, y) subtracts - 1 from y
		for (LinkedList<FinancialCategory> data : categorizedMonths)
		{
			for (FinancialCategory cat : data)
			{
				holder = cat.getSum();
				dataPane.add(SufuFactory.newButton(cat.getCategory(), createBtnName(x, y), evt -> {
					p = MarvusUtils.extractPointFromButtonName(evt);
					targetWindow.updateDataDisplayMode(new MonthCategoryDetail(targetWindow, categorizedMonths.get(p.x).get(p.y), false));
				}));
				dataPane.add(SufuFactory.newTextFieldHeader(holder.toString()));
				if (holder.compareTo(zero) > 0)
					income = income.add(holder);
				else
					spendings = spendings.add(holder);
				y++;
			}
			x++;
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
