package riyufuchi.marvus.app.windows;

import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.app.windows.dialogs.AddDialog;
import riyufuchi.marvus.app.windows.dialogs.SettingsDialog;
import riyufuchi.marvus.app.utils.MarvusCategory;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.CategorizedMonthList;
import riyufuchi.marvus.marvusLib.dataDisplay.CategoryYearTable;
import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataDisplay.MonthList;
import riyufuchi.marvus.marvusLib.dataDisplay.SimpleList;
import riyufuchi.marvus.marvusLib.dataDisplay.SimpleOrderableList;
import riyufuchi.marvus.marvusLib.dataDisplay.YearCategoryList;
import riyufuchi.marvus.marvusLib.dataDisplay.YearOverviewTable;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.utils.DataBox;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.lib.Lib;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 07.09.2023
 * 
 * @author Riyufuchi
 */
public class MarvusMainWindow extends SufuWindow
{
	private CategoryYearTable table;
	private DataDisplayMode dataDisplayMode;
	private CategoryDetailWindow mdt;
	
	public MarvusMainWindow()
	{
		super("Marvus - " + AppTexts.VERSION, 800, 600, false, true, true);
		this.table = new CategoryYearTable(this);
		this.dataDisplayMode = new SimpleList(this, table);
		this.mdt = null;
		MarvusCategory.init();
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(AppTexts.BUDGET_TABLE_MENU, AppTexts.BUDGET_TABLE_MENU_ITEMS, 3);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				// Basic
				case "About" -> jmc.setItemAction(i, event -> about());
				case "Exit" -> jmc.setItemAction(i, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> exportData());
				case "Import" -> jmc.setItemAction(i, event -> importData());
				case "Refresh" -> jmc.setItemAction(i,event -> refresh());
				// Data tools
				case "Sort" -> jmc.setItemAction(i, e -> sortData(TransactionComparation.compareBy(SufuDialogHelper.<CompareMethod>optionDialog(this, "Choose sorting method", "Sorting method chooser", CompareMethod.values()))));
				case "Fix category" -> jmc.setItemAction(i, e -> { MarvusUtils.fixCategory(table.getDataBox()); table.rebuild(); });
				// Tools
				case "Income to outcome" -> jmc.setItemAction(i,event -> setConsumerFunction(TransactionCalculations.incomeToOutcome(DateUtils.showMonthChooser(this).getValue())));
				case "Data summary" -> jmc.setItemAction(i, event -> dataSummary());
				// Data handling
				case "Add" -> jmc.setItemAction(i, event -> add());//new AddDialog(this).showDialog()); // TODO: Optimalize adding to CYT - 4
				// Display modes
				case "Simple list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new SimpleList(this, table)));
				//case "Category list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(DataDisplay.categoryListByMonth(this)));
				case "Month list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new MonthList(this, table)));
				case "Earning/Spending summary" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new YearOverviewTable(this, table, 2023)));
				case "Categorized month list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new CategorizedMonthList(this, table)));
				case "Year category list" -> jmc.setItemAction(i, event -> updateDataDisplayMode(new YearCategoryList(this, table))); 
				// Other
				case "Preferences" -> jmc.setItemAction(i,event -> new SettingsDialog(this).showDialog());
				case "About SufuLib" -> jmc.setItemAction(i, event -> Lib.aboutGUI(this));
				//case "Backup" -> jmc.setItemAction(i,event -> backupData());
				default -> jmc.setItemAction(i, event -> SufuDialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// Utils
	
	/*
	public void displayTable()
	{
		table.rebuild();
		table.displayData();
	}*/
	
	/**
	 * 
	 * @param fc
	 */
	public void showMonthDetailTable(FinancialCategory fc)
	{
		if (mdt != null)
			mdt.dispose();
		mdt = new CategoryDetailWindow(this, fc);
	}
	
	private boolean isOperationUnexucatable()
	{
		if(table.isEmpty())
		{
			SufuDialogHelper.warningDialog(this, "No data to work with!", "No data found");
			return true;
		}
		return false;
	}
	
	// Delegations
	
	private void add()
	{
		/*dataBox.add(new AddDialog(this).showDialogAndGetData());
		refresh();*/
		new AddDialog(this).showDialog();
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	@Deprecated
	private void showMonthDetailTableOld()
	{
		if (isOperationUnexucatable())
			return;
		final int month = DateUtils.showMonthChooser(this).getValue();
		FinancialCategory fc = new FinancialCategory(SufuDialogHelper.<String>categoryDialog(this, "Category:", "Select category", MarvusCategory.names, true));
		table.getDataBox().stream().forEach(t -> {
			if (t.getName().equals(fc.getName()) && t.getDate().getMonthValue() == month)
				fc.add(t);
		});
		mdt = new CategoryDetailWindow(this, fc);
	}
	
	// TODO: Improve data summary - 1
	private void dataSummary()
	{
		String data = "Number of transactions: " + table.size();
		SufuDialogHelper.informationDialog(this, data, "Data summary");
	}
	
	private void sortData(Comparator<Transaction> comp)
	{
		table.getDataBox().setComparator(comp);
		if (dataDisplayMode instanceof SimpleList)
			updateDataDisplayMode(new SimpleOrderableList(this, table));
		table.getDataBox().sort();
		refresh();
	}
	
	private TransactionIO createTransactionIO()
	{
		TransactionIO fio = new TransactionIO(this, MarvusConfig.workFolder);
		fio.setFileFilters(MarvusConfig.SER, MarvusConfig.CSV);
		return fio;
	}
	
	private void exportData()
	{
		if (isOperationUnexucatable())
			return;
		TransactionIO fio = createTransactionIO();
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	private void importData()
	{
		createTransactionIO().showLoadChooser();
		//fio.loadFile();
	}

	private void about()
	{
		SufuDialogHelper.informationDialog(this, AppTexts.ABOUT_MARVUS, "About");
	}
	
	private void updateDataDisplayMode(DataDisplayMode ddm)
	{
		if (ddm == null)
			return;
		dataDisplayMode = ddm;
		if (isOperationUnexucatable())
			return;
		displayData();
	}
	
	/**
	 * Refresh displayed data
	 */
	public void refresh()
	{
		if (isOperationUnexucatable())
			return;
		//dataDisplayMode.refresh();
		displayData(); // Solution for now
		//refreshWindow();
	}
	
	/**
	 * Displays data, use only when changing displayMode or displaying entirely new data
	 */
	public void displayData()
	{
		//displayMode.accept(table.getDataBox());
		getPane().removeAll();
		dataDisplayMode.displayData();
		refreshWindow();
	}
	
	// OnEvent
	
	@Override
	protected void onClose()
	{
		if(SufuDialogHelper.yesNoDialog(this, "Exit application?", "Exit confirmation") == 0)
		{
			if (mdt != null)
				mdt.dispose();
			super.dispose();
		}
	}
	
	// Setters
	
	private void setConsumerFunction(Consumer<DataBox<Transaction>> operation)
	{
		if (isOperationUnexucatable())
			return;
		operation.accept(table.getDataBox());
	}
	
	@Override
	protected void setComponents(JPanel content)
	{
		setupJMenu();
	}
	
	// Getters
	
	public CategoryYearTable getTable()
	{
		return table;
	}
}
