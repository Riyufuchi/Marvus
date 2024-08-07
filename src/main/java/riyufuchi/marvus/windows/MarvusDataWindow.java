package riyufuchi.marvus.windows;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.Marvus;
import riyufuchi.marvus.controller.MarvusController;
import riyufuchi.marvus.controller.MarvusDeleg;
import riyufuchi.marvus.utils.AppTexts;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvus.dialogs.AddDialog;
import riyufuchi.marvus.dialogs.AppManager;
import riyufuchi.marvus.dialogs.PreferencesDialog;
import riyufuchi.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataDisplay.CategorizedMonthList;
import riyufuchi.marvusLib.dataDisplay.CategorizedMonthOverview;
import riyufuchi.marvusLib.dataDisplay.SimpleMonthList;
import riyufuchi.marvusLib.dataDisplay.TimedDetail;
import riyufuchi.marvusLib.dataDisplay.CategorizedYearSummary;
import riyufuchi.marvusLib.dataDisplay.DataSummaryOverview;
import riyufuchi.marvusLib.dataDisplay.MultiYearTable;
import riyufuchi.marvusLib.dataDisplay.YearOverviewTable;
import riyufuchi.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.lib.Lib;
import riyufuchi.sufuLib.lib.SufuAppTools;
import riyufuchi.sufuLib.lib.SufuLib;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @since 18.04.2023
 * @version 20.06.2024
 */
public class MarvusDataWindow extends SufuWindow implements MarvusDataFrame
{
	private MarvusController controller;
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
		URL iconURL = MarvusDataWindow.class.getResource("/riyufuchi/marvus/icon.png");
		if (iconURL != null)
			this.setIconImage(new ImageIcon(iconURL).getImage());
		else
			SufuDialogHelper.errorDialog(this, "Icon image not found!", "Icon IO error");
	}

	
	@Override
	protected void postWindowInit(JPanel content)
	{
		this.controller = new MarvusController(this);
		this.currentMode = new CategorizedMonthList(this);
		this.prevMode = currentMode;
		MarvusDatabase.utils.setParentframe(this);
		if (MarvusConfig.autoLoadData)
			controller.quickOpenFile()
		
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
		SufuMenuCreator jmc = new SufuMenuCreator(AppTexts.BUDGET_TABLE_MENU, AppTexts.BUDGET_TABLE_MENU_ITEMS, 5);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				// File
				case "Open" -> jmc.setItemAction(i, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, event -> controller.quickOpenFile());
				case "Save" -> jmc.setItemAction(i, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, event -> controller.quickSaveFile());
				case "Save As..." -> jmc.setItemAction(i, KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK, event -> controller.saveFile());
				case "Exit" -> jmc.setItemAction(i, KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> controller.exportData());
				case "Import" -> jmc.setItemAction(i, event -> controller.importData());
				case "Refresh" -> jmc.setItemAction(i, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, event -> refresh());
				case "Backup" -> jmc.setItemAction(i, KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK, event -> controller.createBackup());
				// Data tools
				case "Sort" -> jmc.setItemAction(i, e -> sortData());
				case "Fix category" -> jmc.setItemAction(i, e -> { MarvusUtils.fixCategory(this , controller.getDatabase()); });
				// Tools
				case "Month outcome" -> jmc.setItemAction(i,event -> setConsumerFunction(TransactionCalculations.incomeToSpendings(this, SufuDateUtils.showMonthChooser(this))));
				case "Application manager" -> jmc.setItemAction(i, event -> new AppManager(this).showDialog());
				// Data handling
				case "Add" -> jmc.setItemAction(i,  KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, event -> add());
				// Display modes
				//case "Simple list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(new SimpleList(this, table)));
				case "Simple month list" -> jmc.setItemAction(i, KeyEvent.VK_F1,event -> updateDataDisplayMode(new SimpleMonthList(this)));
				case "Categorized month list" -> jmc.setItemAction(i, KeyEvent.VK_F2, event -> updateDataDisplayMode(new CategorizedMonthList(this)));
				case "Categorized month overview" -> jmc.setItemAction(i, KeyEvent.VK_F3, event -> updateDataDisplayMode(new CategorizedMonthOverview(this)));
				case "Categorized year summary" -> jmc.setItemAction(i, KeyEvent.VK_F4, event -> updateDataDisplayMode(new CategorizedYearSummary(this))); 
				case "Earning/Spending summary" -> jmc.setItemAction(i, KeyEvent.VK_F5, event -> updateDataDisplayMode(new YearOverviewTable(this, MarvusConfig.financialYear)));
				case "Data summary" -> jmc.setItemAction(i, KeyEvent.VK_F6, event -> updateDataDisplayMode(new DataSummaryOverview(this)));
				case "Multi year table" -> jmc.setItemAction(i, KeyEvent.VK_F7, event -> updateDataDisplayMode(new MultiYearTable(this)));
				case "Previous mode" -> jmc.setItemAction(i, KeyEvent.VK_ESCAPE, event -> switchDataDisplayMode());
				case "Timed detail" -> jmc.setItemAction(i, KeyEvent.VK_F8, event -> updateDataDisplayMode(new TimedDetail(this)));
				// Window
				case "Preferences" -> jmc.setItemAction(i,event -> new PreferencesDialog(this).showDialog());
				case "Fullscreen" -> jmc.setItemAction(i, KeyEvent.VK_F11, event -> Marvus.fullScreen());
				// Help
				case "About" -> jmc.setItemAction(i, event -> MarvusDeleg.aboutMarvus(this));
				case "About SufuLib" -> jmc.setItemAction(i, event -> Lib.aboutGUI(this));
				case "License" -> jmc.setItemAction(i, event -> SufuAppTools.licenseGUI(this, "/LICENSE.TXT"));
				default -> jmc.setItemAction(i, event -> SufuLib.functionalityNotYetImplementedDialog(this));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// Delegations
	
	private void add()
	{
		new AddDialog(this).showDialog();
		refresh();
	}
	
	private void sortData()
	{
		var res = SufuDialogHelper.<CompareMethod>optionDialog(this, "Choose sorting method", "Sorting method chooser", CompareMethod.values());
		controller.getDatabase().sortData(TransactionComparation.compareBy(res));
		refresh();
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
		if (currentMode.getClass().getName().equals(ddm.getClass().getName()))
		{
			refresh();
			return;
		}
		prevMode = currentMode;
		currentMode = ddm;
		if (controller.isOperationExucatable())
			return;
		displayData();
	}
	
	/**
	 * Refresh displayed data
	 */
	@Override
	public void refresh()
	{
		if (controller.isOperationExucatable())
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
		MarvusUtils.exitApp(this);
	}
	
	// Setters
	
	public void setDatabase(MarvusDatabase database)
	{
		controller.setDatabase(database);
		this.currentMode.setNewData(database);
		this.prevMode.setNewData(database);
		// Because if database is loaded from serialization, comparator and errorHandler will be null
		database.setComparator(TransactionComparation.compareFC(CompareMethod.By_name));
		database.setErrorHandler(s -> SufuDialogHelper.warningDialog(this, s, "Data error"));
	}
	
	private void setConsumerFunction(Consumer<Iterable<Transaction>> consumer)
	{
		if (controller.isOperationExucatable())
			return;
		consumer.accept(controller.getDatabase());
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
		controller.setDatabase(ddm.getDataSource());
		currentMode.setNewData(controller.getDatabase()); // current mode might have still old data table
		ddm.setTargetWindow(this); // ddm have reference to old window
		updateDataDisplayMode(ddm); 
	}
	
	// Getters
	
	public DataDisplayMode getDataDisplayMode()
	{
		return currentMode;
	}
	
	public MarvusDatabase getDatabase()
	{
		return controller.getDatabase();
	}

	@Override
	public JFrame getSelf()
	{
		return this;
	}

	@Override
	public MarvusController getController()
	{
		return controller;
	}
}
