package riyufuchi.marvus.tabs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.tabs.subTabs.TableDetail;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * This tab display transactions in given time span and categorize them by name
 * 
 * @author Riyufuchi
 * @since 18.06.2024
 * @version 09.11.2024
 */
public class TimedDetailTab extends DataDisplayTab
{
	private LocalDateTime fromDate, toDate;
	private JButton dateFrom, dateTo;
	private JComboBox<String> sortByBox;
	private LinkedList<LinkedList<FinancialCategory>> categorizedMonths;
	private JPanel dataPane, menuPane, datePane;
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
		menuPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		datePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
		
		datePane.add(dateFrom);
		datePane.add(new JLabel("to"));
		datePane.add(dateTo);
		
		sortByBox = SufuFactory.<String>newCombobox(MarvusTexts.GROUP_BY_TIME_DETAIL);
		sortByBox.addActionListener(evt -> refresh());
		SufuComponentTools.centerComboboxList(sortByBox);
		menuPane.add(new JLabel("Group by"));
		menuPane.add(sortByBox);
		
		masterPanel.add(menuPane, targetWindow.getGBC(0, 0));
		masterPanel.add(datePane, targetWindow.getGBC(0, 1));
		masterPanel.add(dataPane, targetWindow.getGBC(0, 2));
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
					targetWindow.updateDataDisplayMode(new TableDetail(targetWindow, categorizedMonths.get(p.x).get(p.y), this));
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
		LinkedList<FinancialCategory> llfc = null;
		Iterator<Transaction> it_Transactions;
		Iterator<FinancialCategory> it_finCat;
		Transaction t = null;
		FinancialCategory finCat = null;
		categorizedMonths.clear();
		for (int x = fromDate.getMonthValue(); x <= toDate.getMonthValue(); x++) // This loop takes categorized months and unite categories regardless the dates
		{
			if (sortByBox.getSelectedIndex() == 0)
				llfc = dataSource.getCategorizedMonthByNames(x);
			else
				llfc = dataSource.getCategorizedMonth(x);
			it_finCat = llfc.iterator();
			for (LinkedList<FinancialCategory> data : categorizedMonths)
			{
				for (FinancialCategory fc : data)
				{
					while (it_finCat.hasNext())
					{
						finCat = it_finCat.next();
						if (fc.getCategory().equals(finCat.getCategory()))
						{
							fc.addAll(finCat);
							it_finCat.remove();
						}
					}
				}
			}
			if (llfc.size() != 0)
				categorizedMonths.add(llfc);
		}
		for (LinkedList<FinancialCategory> data : categorizedMonths) // This loop removes transactions outside given date range
		{
			it_finCat = data.iterator();
			while (it_finCat.hasNext())
			{
				finCat = it_finCat.next();
				it_Transactions = finCat.iterator();
				while (it_Transactions.hasNext())
				{
					t = it_Transactions.next();
					if (!((t.getDate().isAfter(fromDate) || t.getDate().equals(fromDate)) && (t.getDate().isBefore(toDate) || t.getDate().equals(toDate))))
						it_Transactions.remove();
				}
				if (finCat.getSum().intValue() == 0)
					it_finCat.remove();
			}
		}
	}
}
