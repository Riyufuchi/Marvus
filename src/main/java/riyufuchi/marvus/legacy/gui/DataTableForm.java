package riyufuchi.marvus.legacy.gui;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvus.dialogs.tools.other.PreferencesDialog;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.legacy.data.MoneySum;
import riyufuchi.marvus.legacy.tabs.MoneySummaryOverview;
import riyufuchi.marvus.legacy.utils.MarvusLegacyHelper;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.marvusLib.dataStorage.MarvusDataBox;
import riyufuchi.sufuLib.gui.ErrorWindow;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.lib.SufuLib;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;

/**
 * @author Riyufuchi
 * @since 11.04.2022
 * @version 30.12.2024
 */
@SuppressWarnings("deprecation")
public final class DataTableForm extends SufuWindow implements MarvusTabbedFrame
{
	public static final String VERSION = "1.22";
	public static final String VERSION_LEGACY = "0.9";
	private static final String TITLE = String.format("Marvus - %s - %s (Legacy)", VERSION, VERSION_LEGACY);
	private MarvusDataBox<MoneySum> dataBox;
	private DataDisplayTab mso;
	
	public DataTableForm()
	{
		super(TITLE, true, true);
		this.dataBox = new MarvusDataBox<>(e -> SufuDialogHelper.exceptionDialog(this, e), byDate());
		this.mso = new MoneySummaryOverview(this, dataBox);
	}
	
	public DataTableForm(int width, int height)
	{
		super(TITLE, width, height, false, true, true);
		this.dataBox = new MarvusDataBox<>(e -> SufuDialogHelper.exceptionDialog(this, e), byDate());
		this.mso = new MoneySummaryOverview(this, dataBox);
	}
	
	private Comparator<MoneySum> byDate()
	{
		return (m1, m2) -> { return m1.getDate().compareTo(m2.getDate()); };
	}

	@Override
	protected void setComponents(JPanel content)
	{
		//content.add(new JButton("Test"), getGBC(0, 0));
		setupJMenu();
	}
	
	@Override
	protected void onClose()
	{
		MarvusGuiUtils.exitApp(this);
	}

	@Override
	public void updateDataDisplayMode(DataDisplayTab dataDisplayMode)
	{
		if (dataDisplayMode == null)
			return;
		mso = dataDisplayMode;
		getPane().removeAll();
		mso.displayData();
		refreshWindow();
	}
	
	public final void loadData(LinkedList<MoneySum> data) throws NullPointerException, IllegalArgumentException
	{
		if(data == null)
			throw new NullPointerException("Inputed datalist is null");
		if(data.isEmpty())
			throw new IllegalArgumentException("Inputed datalist is emtpy");
		dataBox.setList(data);
		getPane().removeAll();
		mso = new MoneySummaryOverview(this, dataBox);
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(MarvusTexts.DTF_MENU, MarvusTexts.DTF_MENU_ITEMS, 3);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				case "About" -> jmc.setItemAction(i, event -> about());
				case "About SufuLib" -> jmc.setItemAction(i, event -> SufuLib.INFO.aboutGUI(this));
				case "Exit" -> jmc.setItemAction(i, KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> exportData());
				case "Import" -> jmc.setItemAction(i, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, event -> importData());
				case "Refresh" -> jmc.setItemAction(i, KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK, event -> refresh());
				case "Count" -> jmc.setItemAction(i,event -> new Counter(this));
				case "Date" -> jmc.setItemAction(i,event -> sort());
				case "Preferences" -> jmc.setItemAction(i,event -> new PreferencesDialog(this).showDialog());
				case "Backup" -> jmc.setItemAction(i, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, event -> backupData());
				default -> jmc.setItemAction(i, event -> SufuDialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void exportData()
	{
		if(dataBox.isEmpty())
		{
			SufuDialogHelper.warningDialog(this, "No data to export", "No data found");
			return;
		}
		FileIO fio = new FileIO(this, MarvusConfig.workFolder);
		fio.setFileFilters(MarvusConfig.CSV, MarvusConfig.SER);
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	private void importData()
	{
		FileIO fio = new FileIO(this, MarvusConfig.workFolder);
		fio.setFileFilters(MarvusConfig.CSV, MarvusConfig.SER);
		fio.setAcceptAllFileFilterUsed(false);
		fio.showLoadChooser();
	}
	
	private void backupData()
	{
		try
		{
			MarvusLegacyHelper.backup(this);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(this, e);
		}
	}
	
	private void sort()
	{
		if(dataBox.isEmpty())
		{
			SufuDialogHelper.warningDialog(this, "No data to sort", "Sort error");
			return;
		}
		dataBox.sort();
		refresh();
	}
	
	public void refresh()
	{
		mso.refresh();
		refreshWindow();
	}
	
	private void about()
	{
		new ErrorWindow("About", 600, 300, "Money manager created by Riyufuchi.\nFinal version: " + VERSION + "\nLegacy update version: " + VERSION_LEGACY + "\n"
			+ "This is leagacy functionality.\nIt will not be updated anymore probably.\nThis app was ment to replace old version that used object DB (JPA), "
			+ "but funtionility become outdated, so rework was needed.");
	}
	
	// GETTERS
	
	public MarvusDataBox<MoneySum> getDataBox()
	{
		return dataBox;
	}

	@Override
	public JFrame getSelf()
	{
		return this;
	}

	@Override
	public void displayData()
	{
	}

	@Override
	public TabController getController()
	{
		return null;
	}

	@Override
	public DataDisplayTab getCurrentTab()
	{
		return mso;
	}

	@Override
	public DataDisplayTab getPreviousTab()
	{
		return null;
	}

	@Override
	public void setDataDisplayMode(DataDisplayTab ddm)
	{
	}
}
