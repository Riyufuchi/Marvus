package riyufuchi.marvus.app;

import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import riyufuchi.marvus.Marvus;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvus.dialogs.tools.other.EntityManagerDialog;
import riyufuchi.marvus.dialogs.tools.other.PreferencesDialog;
import riyufuchi.marvus.interfaces.MarvusFrame;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvusLib.dataCalculations.TransactionCalculations;
import riyufuchi.marvusLib.lib.MarvusLib;
import riyufuchi.sufuLib.gui.SufuWindowTabbedGeneric;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.gui.utils.SufuMenuCreator;
import riyufuchi.sufuLib.interfaces.Fullscreenable;
import riyufuchi.sufuLib.lib.SufuAppTools;
import riyufuchi.sufuLib.lib.SufuLib;
import riyufuchi.sufuLib.time.SufuDateUtils;

/**
 * @author Riyufuchi
 * @since 18.04.2023
 * @version 15.01.2025
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
		super("Marvus - " + MarvusTexts.VERSION, width, height, 800, 600, false, true, true);
		setupJMenu();
		this.controller = new TabController(this);
		newTab(controller.getWorkFile().getName(), controller);
		setTabChangeListener(e -> updateTabController(e));
		if (MarvusConfig.autoLoadData)
		{
			controller.setWorkFile(MarvusConfig.defaultWorkFile);
			if (!controller.quickOpenFile())
			{
				removeLastTab();
				MarvusUtils.createNewTab(this);
			}
		}
		URL iconURL = MarvusDataWindow.class.getClassLoader().getResource("riyufuchi/marvus/icon.png");
		if (iconURL != null)
			this.setIconImage(new ImageIcon(iconURL).getImage());
		else
			SufuDialogHelper.errorDialog(this, "Icon image not found!", "Icon IO error");
	}
	
	private void updateTabController(final int index)
	{
		controller = getCurrentTabController();
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(MarvusTexts.BUDGET_TABLE_MENU, -1, MarvusTexts.BUDGET_TABLE_MENU_ITEMS);
		JMenuItem[] items = jmc.getMenuItems();
		for (JMenuItem item : items)
		{
			switch (item.getText())
			{
				// File
				case "New" -> jmc.setItemAction(item, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK, event -> MarvusUtils.createNewTab(this));
				case "Open" -> jmc.setItemAction(item, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, event -> controller.quickOpenFile());
				case "Save" -> jmc.setItemAction(item, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, event -> controller.quickSaveFile());
				case "Save As..." -> jmc.setItemAction(item, KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK, event -> controller.saveDataToFile());
				case "Exit" -> jmc.setItemAction(item, KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK, event -> onClose());
				case "Export"-> jmc.setItemAction(item, event -> controller.exportData());
				case "Import" -> jmc.setItemAction(item, event -> controller.importData());
				case "Refresh" -> jmc.setItemAction(item, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, event -> controller.refresh());
				case "Backup" -> jmc.setItemAction(item, KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK, event -> controller.createBackup());
				// Data tools
				case "Execute quarry" -> jmc.setItemAction(item, e -> { controller.executeQuarry(); });
				// Tools
				case "Month outcome" -> jmc.setItemAction(item,event -> MarvusUtils.consumeFunction(controller, TransactionCalculations.incomeToSpendings(this, SufuDateUtils.showMonthChooser(this))));
				case "Entity manager" -> jmc.setItemAction(item, event -> new EntityManagerDialog(this).showDialog());
				// Data handling
				case "Add" -> jmc.setItemAction(item, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, event -> controller.addNewTransaction());
				// Display modes
				case "Table list" -> jmc.setItemAction(item, KeyEvent.VK_F1,event -> controller.updateDataDisplayMode(0));
				case "Categorized month list" -> jmc.setItemAction(item, KeyEvent.VK_F2, event -> controller.updateDataDisplayMode(1));
				case "Uncategorized month list" -> jmc.setItemAction(item, KeyEvent.VK_F3, event -> controller.updateDataDisplayMode(2));
				case "Year summary" -> jmc.setItemAction(item, KeyEvent.VK_F4, event -> controller.updateDataDisplayMode(3)); 
				case "Year overview" -> jmc.setItemAction(item, KeyEvent.VK_F5, event -> controller.updateDataDisplayMode(4));
				case "Data summary" -> jmc.setItemAction(item, KeyEvent.VK_F6, event -> controller.updateDataDisplayMode(5));
				case "Week detail" -> jmc.setItemAction(item, KeyEvent.VK_F7, event -> controller.updateDataDisplayMode(6));
				case "Previous mode" -> jmc.setItemAction(item, KeyEvent.VK_ESCAPE, event -> controller.switchDataDisplayMode());
				// Window
				case "Preferences" -> jmc.setItemAction(item,event -> new PreferencesDialog(this).showDialog());
				case "Fullscreen" -> jmc.setItemAction(item, KeyEvent.VK_F11, event -> Marvus.fullScreen());
				// Help
				case "About" -> jmc.setItemAction(item, event -> MarvusUtils.aboutMarvus(this));
				case "About SufuLib" -> jmc.setItemAction(item, event -> SufuLib.INFO.aboutGUI(this));
				case "About MarvusLib" -> jmc.setItemAction(item, event -> MarvusLib.INFO.aboutGUI(this));
				case "License" -> jmc.setItemAction(item, event -> SufuAppTools.licenseGUI(this, "/LICENSE.TXT"));
				default -> jmc.setItemAction(item, event -> SufuDialogHelper.functionalityNotYetImplementedDialog(this));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// OnEvent
	
	@Override
	protected void onClose()
	{
		if (controller != null)
			controller.saveChanges();
		MarvusGuiUtils.exitApp(this);
	}
	
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
