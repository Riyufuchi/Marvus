package riyufuchi.marvus.app;

import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import riyufuchi.marvus.Marvus;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvus.dialogs.AppManager;
import riyufuchi.marvus.dialogs.PreferencesDialog;
import riyufuchi.marvus.tabs.CategorizedMonthListTab;
import riyufuchi.marvus.tabs.UncategorizedMonthListTab;
import riyufuchi.marvus.tabs.YearSummaryTab;
import riyufuchi.marvus.tabs.DataSummaryTab;
import riyufuchi.marvus.tabs.SimpleMonthListTab;
import riyufuchi.marvus.tabs.TimedDetailTab;
import riyufuchi.marvus.tabs.YearOverviewTab;
import riyufuchi.marvusLib.dataUtils.TransactionCalculations;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.Fullscreenable;
import riyufuchi.marvusLib.interfaces.MarvusFrame;
import riyufuchi.sufuLib.gui.SufuWindowTabbedGeneric;
import riyufuchi.sufuLib.lib.Lib;
import riyufuchi.sufuLib.lib.SufuAppTools;
import riyufuchi.sufuLib.lib.SufuLib;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @since 18.04.2023
 * @version 09.09.2024
 */
public class MarvusDataWindow extends SufuWindowTabbedGeneric<TabController> implements MarvusFrame, Fullscreenable<MarvusDataWindow>
{
	private TabController controller;
	
	/**
	 * Creates window in fullscreen mode
	 */
	public MarvusDataWindow()
	{
		this(0, 0);
	}
	
	public MarvusDataWindow(int width, int height)
	{
		super("Marvus - " + MarvusTexts.VERSION, width, height, false, true, true);
		setupJMenu();
		this.controller = new TabController(this);
		newTab(controller);
		setTabChangeListener(e -> updateTabController(e));
		MarvusDatabase.utils.setParentframe(this);
		if (MarvusConfig.autoLoadData)
			if (!controller.quickOpenFile())
				removeLastTab();
		URL iconURL = MarvusDataWindow.class.getClassLoader().getResource("riyufuchi/marvus/icon.png");
		if (iconURL != null)
			this.setIconImage(new ImageIcon(iconURL).getImage());
		else
			SufuDialogHelper.errorDialog(this, "Icon image not found!", "Icon IO error");
	}
	
	private void updateTabController(final int index)
	{
		controller = getCurrentTabController();
		MarvusConfig.currentFinancialYear = controller.getFinancialYear();
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(MarvusTexts.BUDGET_TABLE_MENU, MarvusTexts.BUDGET_TABLE_MENU_ITEMS, 5);
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
				case "Refresh" -> jmc.setItemAction(i, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, event -> controller.refresh());
				case "Backup" -> jmc.setItemAction(i, KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK, event -> controller.createBackup());
				// Data tools
				case "Sort" -> jmc.setItemAction(i, e -> controller.sortData());
				case "Execute quarry" -> jmc.setItemAction(i, e -> {controller.executeQuarry(); });
				// Tools
				case "Month outcome" -> jmc.setItemAction(i,event -> MarvusUtils.consumeFunction(controller, TransactionCalculations.incomeToSpendings(this, SufuDateUtils.showMonthChooser(this))));
				case "Application manager" -> jmc.setItemAction(i, event -> new AppManager(this).showDialog());
				// Data handling
				case "Add" -> jmc.setItemAction(i,  KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, event -> controller.addNewTransaction());
				// Display modes
				case "Simple month list" -> jmc.setItemAction(i, KeyEvent.VK_F1,event -> controller.updateDataDisplayMode(new SimpleMonthListTab(controller)));
				case "Categorized month list" -> jmc.setItemAction(i, KeyEvent.VK_F2, event -> controller.updateDataDisplayMode(new CategorizedMonthListTab(controller)));
				case "Uncategorized month list" -> jmc.setItemAction(i, KeyEvent.VK_F3, event -> controller.updateDataDisplayMode(new UncategorizedMonthListTab(controller)));
				case "Year summary" -> jmc.setItemAction(i, KeyEvent.VK_F4, event -> controller.updateDataDisplayMode(new YearSummaryTab(controller))); 
				case "Year overview" -> jmc.setItemAction(i, KeyEvent.VK_F5, event -> controller.updateDataDisplayMode(new YearOverviewTab(controller, MarvusConfig.currentFinancialYear)));
				case "Data summary" -> jmc.setItemAction(i, KeyEvent.VK_F6, event -> controller.updateDataDisplayMode(new DataSummaryTab(controller)));
				case "Week detail" -> jmc.setItemAction(i, KeyEvent.VK_F7, event -> controller.updateDataDisplayMode(new TimedDetailTab(controller)));
				case "Previous mode" -> jmc.setItemAction(i, KeyEvent.VK_ESCAPE, event -> controller.switchDataDisplayMode());
				// Window
				case "Preferences" -> jmc.setItemAction(i,event -> new PreferencesDialog(this).showDialog());
				case "Fullscreen" -> jmc.setItemAction(i, KeyEvent.VK_F11, event -> Marvus.fullScreen());
				// Help
				case "About" -> jmc.setItemAction(i, event -> MarvusUtils.aboutMarvus(this));
				case "About SufuLib" -> jmc.setItemAction(i, event -> Lib.aboutGUI(this));
				case "License" -> jmc.setItemAction(i, event -> SufuAppTools.licenseGUI(this, "/LICENSE.TXT"));
				default -> jmc.setItemAction(i, event -> SufuLib.functionalityNotYetImplementedDialog(this));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// OnEvent
	
	@Override
	protected void onClose()
	{
		MarvusGuiUtils.exitApp(this);
	}
	
	// Setters
	
	// Getters

	@Override
	public JFrame getSelf()
	{
		return this;
	}

	@Override
	public TabController getController()
	{
		return controller;
	}

	@Override
	public void toggleFullscreen(MarvusDataWindow oldFrame)
	{
		controller.setDataDisplayMode(oldFrame.getController().getCurrentTab());
		controller.displayData();
	}
}
