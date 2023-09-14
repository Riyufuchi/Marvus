package riyufuchi.marvus.app.windows;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusMain;
import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusIO;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.app.windows.dialogs.AddDialog;
import riyufuchi.marvus.app.windows.dialogs.SettingsDialog;
import riyufuchi.marvus.app.utils.MarvusCategory;
import riyufuchi.marvus.marvusLib.data.FinancialCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.CategorizedMonthList;
import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataDisplay.MonthList;
import riyufuchi.marvus.marvusLib.dataDisplay.SimpleList;
import riyufuchi.marvus.marvusLib.dataDisplay.SimpleOrderableList;
import riyufuchi.marvus.marvusLib.dataDisplay.YearCategoryList;
import riyufuchi.marvus.marvusLib.dataDisplay.YearOverviewTable;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.marvus.marvusLib.dataStorage.DataBox;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.financialRecords.DataSummary;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.lib.Lib;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 14.09.2023
 * 
 * @author Riyufuchi
 */
public class MarvusDataWindow extends SufuWindow
{
	private TransactionDataTable table;
	private DataDisplayMode dataDisplayMode;
	private CategoryDetailWindow mdt;
	private File curretntDataFile;
	
	/**
	 * Creates window in fullscreen mode
	 */
	public MarvusDataWindow()
	{
		super("Marvus - " + AppTexts.VERSION, false, true);
		postWindowInit(getPane());
	}
	
	public MarvusDataWindow(int width, int height)
	{
		super("Marvus - " + AppTexts.VERSION, width, height, false, true, true);
		postWindowInit(getPane());
	}
	
	@Override
	protected void postWindowInit(JPanel content)
	{
		this.table = new TransactionDataTable(this);
		this.dataDisplayMode = new SimpleList(this, table);
		this.mdt = null;
		MarvusCategory.init();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				//if (e.getKeyCode() == KeyEvent.VK_F11)
						//MarvusMain.fullScreen();
				return false;
			}
		});
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(AppTexts.BUDGET_TABLE_MENU, AppTexts.BUDGET_TABLE_MENU_ITEMS, 4);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				// File
				case "Open" -> jmc.setItemAction(i, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, event -> importData());
				case "Save" -> jmc.setItemAction(i, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, event -> quickSaveFile());
				case "Exit" -> jmc.setItemAction(i, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> exportData());
				case "Import" -> jmc.setItemAction(i, event -> importData());
				case "Refresh" -> jmc.setItemAction(i, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, event -> refresh());
				// Data tools
				case "Sort" -> jmc.setItemAction(i, e -> sortData(TransactionComparation.compareBy(SufuDialogHelper.<CompareMethod>optionDialog(this, "Choose sorting method", "Sorting method chooser", CompareMethod.values()))));
				case "Fix category" -> jmc.setItemAction(i, e -> { MarvusUtils.fixCategory(this ,table.getDataBox()); table.rebuild(); });
				// Tools
				case "Income to outcome" -> jmc.setItemAction(i,event -> setConsumerFunction(TransactionCalculations.incomeToOutcome(SufuDateUtils.showMonthChooser(this).getValue())));
				case "Data summary" -> jmc.setItemAction(i, event -> dataSummary());
				// Data handling
				case "Add" -> jmc.setItemAction(i,  KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, event -> add());//new AddDialog(this).showDialog()); // TODO: Optimalize adding to CYT - 4
				// Display modes
				case "Simple list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new SimpleList(this, table)));
				//case "Category list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(DataDisplay.categoryListByMonth(this)));
				case "Month list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new MonthList(this, table)));
				case "Earning/Spending summary" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new YearOverviewTable(this, table, 2023)));
				case "Categorized month list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new CategorizedMonthList(this, table)));
				case "Year category list" -> jmc.setItemAction(i, event -> updateDataDisplayMode(new YearCategoryList(this, table))); 
				// Window
				case "Preferences" -> jmc.setItemAction(i,event -> new SettingsDialog(this).showDialog());
				case "Fullscreen" -> jmc.setItemAction(i, KeyEvent.VK_F11, event -> MarvusMain.fullScreen());
				// Help
				case "About" -> jmc.setItemAction(i, event -> about());
				case "About SufuLib" -> jmc.setItemAction(i, event -> Lib.aboutGUI(this));
				default -> jmc.setItemAction(i, event -> SufuDialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// Utils
	
	/**
	 * 
	 * @param fc
	 */
	public void showMonthDetailTable(FinancialCategory fc, boolean dynamic)
	{
		if (mdt != null)
			mdt.dispose();
		mdt = new CategoryDetailWindow(this, fc, dynamic);
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
		final int month = riyufuchi.marvus.marvusLib.legacy.DateUtils.showMonthChooser(this).getValue();
		FinancialCategory fc = new FinancialCategory(SufuDialogHelper.<String>categoryDialog(this, "Category:", "Select category", MarvusCategory.names, true));
		table.getDataBox().stream().forEach(t -> {
			if (t.getName().equals(fc.getName()) && t.getDate().getMonthValue() == month)
				fc.add(t);
		});
		mdt = new CategoryDetailWindow(this, fc, true);
	}
	
	private void dataSummary()
	{
		DataSummary ds = table.getDataSummary(2023);
		String data = "Total transactions: %d (avg: %.2f)\n"
				+ "Total income: %.2f (avg: %.2f)\n"
				+ "Total spendings: %.2f (avg: %.2f)\n"
				+ "Total outcome: %.2f (avg: %.2f)\n";
		data = String.format(data, ds.transactionsTotal(), ds.avgTransactionPerYear(),
				ds.totalIncome(), ds.avgIncome(),
				ds.totalSpendigs(), ds.avgSpendings(),
				ds.totalRatio(), ds.avgTotal());
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
	
	private void quickSaveFile()
	{
		if (curretntDataFile != null)
			MarvusIO.quickSave(this, curretntDataFile.getAbsolutePath(), table.getDataBox().getList());
		else
			SufuDialogHelper.warningDialog(this, "No save destination found!", "No save destination");
	}
	
	private void importData()
	{
		curretntDataFile = createTransactionIO().showLoadChooser();
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
		dataDisplayMode.refresh();
		refreshWindow();
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
	
	public void setTable(TransactionDataTable table)
	{
		this.table = table;
		this.dataDisplayMode = new SimpleList(this, table);
	}
	
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
	
	public TransactionDataTable getTable()
	{
		return table;
	}
}
