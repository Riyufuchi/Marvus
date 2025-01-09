package riyufuchi.marvus.tabs;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import riyufuchi.marvus.database.MarvusConnection;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.marvusLib.interfaces.MarvusQuerriable;
import riyufuchi.marvusLib.records.MarvusCategoryStatistic;
import riyufuchi.marvusLib.records.MarvusDataStatistics;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.gui.utils.SufuFactory;

/**
 * @author riyufuchi
 * @since ?
 * @version 08.01.2025
 */
public class DataSummaryTab extends DataDisplayTab
{
	private final String[] CAPTIONS;
	private final String FORMAT;
	private final int YEAR;
	private int xIndex, yIndex;
	private MarvusQuerriable con;
	private MarvusDataStatistics ds;
	private MarvusCategoryStatistic mcs;
	private JComboBox<String> categoryOption;
	private JTextField category;
	
	public DataSummaryTab(MarvusTabbedFrame targetWindow, int year)
	{
		super(targetWindow);
		this.CAPTIONS = new String[]{"Transactions", "Income", "Spendings", "Outcome"};
		this.FORMAT = "%.2f";
		this.YEAR = year;
		this.con = new MarvusConnection(database);
		this.ds = con.createDataStatistics(YEAR);
		// UI
		this.categoryOption = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.categories.getData()), evt -> refresh());
		addMenuAndMenuItems(categoryOption);
		addContentPanel();
		this.mcs = con.createCategoryStatistic(SufuComponentTools.extractComboboxValue(categoryOption), YEAR);
		this.category = SufuFactory.newTextFieldHeader(mcs.category());
		resetValues();
		this.yIndex = 1;
		for (String s : CAPTIONS)
			contentPanel.add(SufuFactory.newTextFieldHeader(s), contentPanel.getGBC(0, yIndex++));
		contentPanel.add(category, contentPanel.getGBC(0, yIndex));
		contentPanel.add(SufuFactory.newTextFieldHeader(String.valueOf(YEAR)), contentPanel.getGBC(0, 0));
		contentPanel.add(SufuFactory.newTextFieldHeader("Year total"), contentPanel.getGBC(1, 0));
		contentPanel.add(SufuFactory.newTextFieldHeader("Year average"), contentPanel.getGBC(2, 0));
		contentPanel.add(SufuFactory.newTextFieldHeader("Daily average"), contentPanel.getGBC(3, 0));
		resetValues();
		categoryOption.setSelectedIndex(0);
	}

	@Override
	public void displayData()
	{
		// Year total values
		addInfoItem(String.valueOf(ds.transactionsTotal()));
		addInfoItem(String.valueOf(ds.totalIncome()));
		addInfoItem(String.valueOf(ds.totalSpendigs().abs()));
		addInfoItem(String.valueOf(ds.totalOutcome()));
		addInfoItem(String.valueOf(mcs.total()));
		// Avg. year values
		newColumn();
		addInfoItem(String.format(FORMAT, ds.avgTransactionsPerYear()));
		addInfoItem(String.format(FORMAT, ds.avgIncome()));
		addInfoItem(String.format(FORMAT, ds.avgSpendings().abs()));
		addInfoItem(String.format(FORMAT, ds.avgOutcome()));
		addInfoItem(String.format(FORMAT, mcs.yearAverage()));
		// Avg. daily values
		newColumn();
		addInfoItem(String.format(FORMAT, ds.avgTransactionsPerDay()));
		addInfoItem(String.format(FORMAT, ds.avdDailyIncome()));
		addInfoItem(String.format(FORMAT, ds.avgDailySpendings().abs()));
		addInfoItem(String.format(FORMAT, ds.avgDailyOutcome()));
		addInfoItem(String.format(FORMAT, mcs.dailyAverage()));
		resetValues();
	}

	@Override
	public void refresh()
	{
		clearPanel(contentPanel, 8);
		resetValues();
		ds = con.createDataStatistics(YEAR);
		mcs = con.createCategoryStatistic(SufuComponentTools.extractComboboxValue(categoryOption), YEAR);
		category.setText(mcs.category());
		displayData();
	}
	
	private void resetValues()
	{
		yIndex = 0;
		xIndex = 1;
	}
	
	private void newColumn()
	{
		yIndex = 0;
		xIndex++;
	}
	
	private void addInfoItem(String info)
	{
		yIndex++;
		contentPanel.add(SufuFactory.newTextFieldHeader(info), contentPanel.getGBC(xIndex, yIndex));
	}
}
