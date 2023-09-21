package riyufuchi.marvus.app.windows;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusIO;
import riyufuchi.marvus.app.utils.MarvusMainThread;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.app.windows.dialogs.AddDialog;
import riyufuchi.marvus.app.windows.dialogs.PreferencesDialog;
import riyufuchi.marvus.app.utils.MarvusCategory;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataDisplay.CategorizedMonthList;
import riyufuchi.marvus.marvusLib.dataDisplay.CategorizedMonthOverview;
import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataDisplay.SimpleMonthList;
import riyufuchi.marvus.marvusLib.dataDisplay.CategorizedYearSummary;
import riyufuchi.marvus.marvusLib.dataDisplay.YearOverviewTable;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.marvus.marvusLib.dataStorage.DataBox;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.financialRecords.DataSummary;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.lib.Lib;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 19.09.2023
 * 
 * @author Riyufuchi
 */
public class MarvusDataWindow extends SufuWindow implements MarvusDataFrame
{
	private TransactionDataTable table;
	private DataDisplayMode currentMode, prevMode;
	
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
		this.currentMode = new CategorizedMonthList(this, table);
		this.prevMode = currentMode;
		MarvusCategory.init();
		/*KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			@Override
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_F11)
						MarvusMain.fullScreen();
				return false;
			}
		})*/;
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
				case "Open" -> jmc.setItemAction(i, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, event -> quickOpenFile());
				case "Save" -> jmc.setItemAction(i, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, event -> quickSaveFile());
				case "Exit" -> jmc.setItemAction(i, KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> exportData());
				case "Import" -> jmc.setItemAction(i, event -> importData());
				case "Refresh" -> jmc.setItemAction(i, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, event -> refresh());
				// Data tools
				case "Sort" -> jmc.setItemAction(i, e -> sortData(TransactionComparation.compareBy(SufuDialogHelper.<CompareMethod>optionDialog(this, "Choose sorting method", "Sorting method chooser", CompareMethod.values()))));
				case "Fix category" -> jmc.setItemAction(i, e -> { MarvusUtils.fixCategory(this ,table.getDataBox()); table.rebuild(); });
				// Tools
				case "Month outcome" -> jmc.setItemAction(i,event -> setConsumerFunction(TransactionCalculations.incomeToOutcome(SufuDateUtils.showMonthChooser(this).getValue())));
				case "Data summary" -> jmc.setItemAction(i, event -> dataSummary());
				// Data handling
				case "Add" -> jmc.setItemAction(i,  KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, event -> add());
				// Display modes
				//case "Simple list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new SimpleList(this, table)));
				case "Simple month list" -> jmc.setItemAction(i, KeyEvent.VK_F1,event -> updateDataDisplayMode(new SimpleMonthList(this, table)));
				case "Categorized month list" -> jmc.setItemAction(i, KeyEvent.VK_F2, event -> updateDataDisplayMode(new CategorizedMonthList(this, table)));
				case "Categorized month overview" -> jmc.setItemAction(i, KeyEvent.VK_F3, event -> updateDataDisplayMode(new CategorizedMonthOverview(this, table)));
				case "Categorized year summary" -> jmc.setItemAction(i, KeyEvent.VK_F4, event -> updateDataDisplayMode(new CategorizedYearSummary(this, table))); 
				case "Earning/Spending summary" -> jmc.setItemAction(i, KeyEvent.VK_F5, event -> updateDataDisplayMode(new YearOverviewTable(this, table, 2023)));
				case "Previous mode" -> jmc.setItemAction(i, KeyEvent.VK_ESCAPE, event -> switchDataDisplayMode());
				// Window
				case "Preferences" -> jmc.setItemAction(i,event -> new PreferencesDialog(this).showDialog());
				case "Fullscreen" -> jmc.setItemAction(i, KeyEvent.VK_F11, event -> MarvusMainThread.fullScreen());
				// Help
				case "About" -> jmc.setItemAction(i, event -> about());
				case "About SufuLib" -> jmc.setItemAction(i, event -> Lib.aboutGUI(this));
				default -> jmc.setItemAction(i, event -> SufuDialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// Utils
	
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
	
	private void quickOpenFile()
	{
		if (MarvusConfig.currentWorkFile == null)
		{
			importData();
			return;
		}
		try
		{
			SufuFileHelper.checkFile(MarvusConfig.currentWorkFile.getAbsolutePath());
			table.getDataBox().setList(MarvusIO.loadData(this, MarvusConfig.currentWorkFile.getAbsolutePath()));
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(this, e);
		}
		table.rebuild();
		displayData();
	}
	
	private void add()
	{
		new AddDialog(this).showDialog();
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
				ds.totalOutcome(), ds.avgOutcome());
		SufuDialogHelper.informationDialog(this, data, "Data summary");
	}
	
	private void sortData(Comparator<Transaction> comp)
	{
		table.getDataBox().setComparator(comp);
		table.getDataBox().sort();
		refresh();
	}
	
	private void exportData()
	{
		if (isOperationUnexucatable())
			return;
		TransactionIO fio = MarvusUtils.createTransactionIO(this);
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	private void quickSaveFile()
	{
		if (MarvusConfig.currentWorkFile != null)
			MarvusIO.quickSave(this, MarvusConfig.currentWorkFile.getAbsolutePath(), table.getDataBox().getList());
		else
			SufuDialogHelper.warningDialog(this, "No save destination found!", "No save destination");
	}
	
	private void importData()
	{
		MarvusConfig.currentWorkFile =  MarvusUtils.createTransactionIO(this).showLoadChooser();
	}

	private void about()
	{
		SufuDialogHelper.informationDialog(this, AppTexts.ABOUT_MARVUS, "About");
	}
	
	private void switchDataDisplayMode()
	{
		DataDisplayMode mode = currentMode;
		currentMode = prevMode;
		prevMode = mode;
		displayData();
	}
	
	@Override
	public void updateDataDisplayMode(DataDisplayMode ddm)
	{
		if (ddm == null)
			return;
		prevMode = currentMode;
		currentMode = ddm;
		if (isOperationUnexucatable())
			return;
		displayData();
	}
	
	/**
	 * Refresh displayed data
	 */
	@Override
	public void refresh()
	{
		if (isOperationUnexucatable())
			return;
		currentMode.refresh();
		refreshWindow();
	}
	
	/**
	 * Displays data, use only when changing displayMode or displaying entirely new data
	 */
	public void displayData()
	{
		//displayMode.accept(table.getDataBox());
		getPane().removeAll();
		currentMode.displayData();
		refreshWindow();
	}
	
	// OnEvent
	
	@Override
	protected void onClose()
	{
		int result = 0;
		if (MarvusConfig.showQuitDialog)
			result = SufuDialogHelper.yesNoDialog(this, "Do you really want to exit the application?", "Exit confirmation");
		if (result == 0)
			super.dispose();
		
	}
	
	// Setters
	
	public void setTable(TransactionDataTable table)
	{
		this.table = table;
		this.currentMode.setNewData(table);
		this.prevMode.setNewData(table);
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
	
	/**
	 * This setter sets data disply mode when changing fullscreen and windowed mode
	 * 
	 * @param ddm
	 */
	public void setDataDisplayMode(DataDisplayMode ddm)
	{
		if (ddm == null)
			return;
		table = ddm.getDataSource();
		currentMode.setNewData(table); // current mode might have still old data table
		ddm.setTargetWindow(this); // ddm have reference to old window
		updateDataDisplayMode(ddm); 
	}
	
	// Getters
	
	public DataDisplayMode getDataDisplayMode()
	{
		return currentMode;
	}
	
	public TransactionDataTable getTable()
	{
		return table;
	}

	@Override
	public JFrame getSelf()
	{
		return this;
	}
}
