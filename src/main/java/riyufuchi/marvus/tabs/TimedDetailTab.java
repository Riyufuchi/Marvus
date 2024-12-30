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
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.TableDetail;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.FinancialCategory;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * This tab display transactions in given time span and categorize them by name
 * 
 * @author Riyufuchi
 * @since 18.06.2024
 * @version 29.11.2024
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
		// Creating UI
		this.menuPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.datePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.dataPane = new JPanel(new GridLayout(0, 2));
		this.sortByBox = SufuFactory.<String>newCombobox(MarvusTexts.GROUP_BY_TIME_DETAIL);
		this.sortByBox.addActionListener(evt -> refresh());
		SufuComponentTools.centerComboboxList(sortByBox);
		this.dateFrom = SufuFactory.newButton("", evt -> {
			fromDate = new SufuDatePicker(targetWindow.getSelf(), fromDate).showAndGet();
			MarvusGuiUtils.editDateText(dateFrom, fromDate);
			refresh();
		}); 
		this.dateTo = SufuFactory.newButton("", evt -> {
			toDate = new SufuDatePicker(targetWindow.getSelf(), toDate).showAndGet();
			MarvusGuiUtils.editDateText(dateTo, toDate);
			refresh();
		}); 
		MarvusGuiUtils.editDateText(dateFrom, fromDate);
		MarvusGuiUtils.editDateText(dateTo, toDate);
		this.datePane.add(dateFrom);
		this.datePane.add(new JLabel("to"));
		this.datePane.add(dateTo);
		this.menuPane.add(new JLabel("Group by"));
		this.menuPane.add(sortByBox);
		masterPanel.add(menuPane, masterPanel.getGBC(0, 0));
		masterPanel.add(datePane, masterPanel.getGBC(0, 1));
		masterPanel.add(dataPane, masterPanel.getGBC(0, 2));
		prepData();
	}

	@Override
	public void displayData()
	{
		refresh();
	}

	@Override
	public void refresh()
	{
		dataPane.removeAll();
		dataPane.revalidate();
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
