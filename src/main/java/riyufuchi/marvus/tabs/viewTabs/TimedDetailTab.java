package riyufuchi.marvus.tabs.viewTabs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.subTabs.TableDetail;
import riyufuchi.marvus.tabs.utils.DataDisplayTab;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.FinancialCategorySafe;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.general.SufuInterval;

/**
 * This tab display transactions in given time span and categorize them by name
 * 
 * @author Riyufuchi
 * @since 18.06.2024
 * @version 17.01.2025
 */
public class TimedDetailTab extends DataDisplayTab
{
	private JButton dateFrom, dateTo;
	private JComboBox<String> sortByBox;
	private LinkedList<FinancialCategorySafe> categories;
	private JPanel dataPane, menuPane, datePane;
	private Point p;
	private SufuInterval<LocalDateTime> dateInerval;

	public TimedDetailTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.dateInerval = new SufuInterval<>(LocalDateTime.now().minusDays(7).toLocalDate().atStartOfDay(),
				LocalDateTime.now().toLocalDate().atStartOfDay()); // So we get data for past 7 days by default
		this.categories = new LinkedList<>();
		// Creating UI
		this.menuPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.datePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.dataPane = new JPanel(new GridLayout(0, 2));
		this.sortByBox = SufuFactory.<String>newCombobox(MarvusTexts.GROUP_BY_TIME_DETAIL);
		this.sortByBox.addActionListener(evt -> refresh());
		SufuComponentTools.centerComboboxList(sortByBox);
		this.dateFrom = SufuFactory.newButton("", evt -> {
			dateInerval.setMin(new SufuDatePicker(targetWindow.getSelf(), dateInerval.getMin()).showAndGet());
			MarvusGuiUtils.editDateText(dateFrom, dateInerval.getMin());
			refresh();
		}); 
		this.dateTo = SufuFactory.newButton("", evt -> {
			dateInerval.setMax(new SufuDatePicker(targetWindow.getSelf(), dateInerval.getMax()).showAndGet());
			MarvusGuiUtils.editDateText(dateTo, dateInerval.getMax());
			refresh();
		}); 
		MarvusGuiUtils.editDateText(dateFrom, dateInerval.getMin());
		MarvusGuiUtils.editDateText(dateTo, dateInerval.getMax());
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
		for (FinancialCategorySafe cat : categories)
		{
			holder = cat.getSum();
			dataPane.add(SufuFactory.newButton(cat.getCategory(), MarvusGuiUtils.encodeCords(x, y), evt -> {
				p = MarvusGuiUtils.extractPointFromButtonName(evt);
				targetWindow.updateDataDisplayMode(new TableDetail(targetWindow, categories.get(p.y), this));
			}));
			dataPane.add(SufuFactory.newTextFieldHeader(holder.toString()));
			if (holder.compareTo(zero) > 0)
				income = income.add(holder);
			else
				spendings = spendings.add(holder);
			y++;
		}
		dataPane.add(SufuFactory.newTextFieldHeader("Income:"));
		dataPane.add(SufuFactory.newTextFieldHeader(income.toString()));
		dataPane.add(SufuFactory.newTextFieldHeader("Spendings:"));
		dataPane.add(SufuFactory.newTextFieldHeader(spendings.toString()));
		dataPane.add(SufuFactory.newTextFieldHeader("Outcome:"));
		dataPane.add(SufuFactory.newTextFieldHeader(income.add(spendings).toString()));
	}
	
	private LinkedList<FinancialCategorySafe> selectAndGetCategorizedMonth(Month month)
	{
		if (sortByBox.getSelectedIndex() == 0)
			return database.getCategorizedMonthByNames(month);
		return database.getCategorizedMonth(month);
	}
	
	private void prepData()
	{
		categories.clear();
		final Transaction BREAKER = new Transaction();
		final SufuInterval<Integer> interval = new SufuInterval<>(dateInerval.getMin().getMonthValue(), dateInerval.getMax().getMonthValue());
		int month = interval.getMin();
		Iterator<FinancialCategorySafe> it_categories = null;
		FinancialCategorySafe financialCategoryHolder = null;
		LinkedList<FinancialCategorySafe> categorizedMonth = null;
		// This loop takes categorized months and unite categories regardless the dates
		do
		{
			categorizedMonth = selectAndGetCategorizedMonth(MarvusUtils.monthTable.getByID(month).get());
			for (FinancialCategorySafe financialCategory : categorizedMonth)
			{
				it_categories = categories.iterator();
				while (it_categories.hasNext())
				{
					financialCategoryHolder = it_categories.next();
					if (financialCategoryHolder.getCategory().equals(financialCategory.getCategory()))
					{
						financialCategoryHolder.addAll(financialCategory);
						financialCategory.set(0, BREAKER);
						break;
					}
				}
				if (financialCategory.getFirst().getID() != -1)
					categories.add(financialCategory);
			}
			month++;
		} while (interval.isIn(month));
		// Clear transactions
		categories.stream().forEach(category -> category.removeIf(transaction -> dateInerval.isNotIn(transaction.getDate())));
		// Clear empty categories
		categories.removeIf(category -> category.isEmpty());
	}
}
