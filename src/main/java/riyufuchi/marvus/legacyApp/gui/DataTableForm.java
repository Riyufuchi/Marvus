package riyufuchi.marvus.legacyApp.gui;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.windows.dialogs.PreferencesDialog;
import riyufuchi.marvus.legacyApp.utils.MarvusLegacyHelper;
import riyufuchi.marvus.legacyApp.utils.MoneySum;
import riyufuchi.marvus.legacyApp.utils.MoneySummaryOverview;
import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.marvus.marvusLib.dataStorage.DataBox;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.gui.ErrorWindow;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.lib.Lib;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;

/**
 * Created On: 11.04.2022<br>
 * Last Edit: 25.09.2023
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("deprecation")
public final class DataTableForm extends SufuWindow implements MarvusDataFrame
{
	private DataBox<MoneySum> dataBox;
	private DataDisplayMode mso;
	
	public DataTableForm()
	{
		super("Marvus - Data table", true, true);
		this.dataBox = new DataBox<>(e -> SufuDialogHelper.exceptionDialog(this, e), byDate());
		this.mso = new MoneySummaryOverview(this, new TransactionDataTable(this), dataBox);
	}
	
	public DataTableForm(int width, int height)
	{
		super("Marvus - Data table", width, height, false, true, true);
		this.dataBox = new DataBox<>(e -> SufuDialogHelper.exceptionDialog(this, e), byDate());
		this.mso = new MoneySummaryOverview(this, new TransactionDataTable(this), dataBox);
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
		int result = 0;
		if (MarvusConfig.showQuitDialog)
			result = SufuDialogHelper.yesNoDialog(this, "Do you really want to exit the application?", "Exit confirmation");
		if (result == 0)
			super.dispose();
	}

	@Override
	public void updateDataDisplayMode(DataDisplayMode dataDisplayMode)
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
			throw new NullPointerException("Inputed datalist was null");
		if(data.isEmpty())
			throw new IllegalArgumentException("Inputed datalist is emtpy");
		dataBox.setList(data);
		getPane().removeAll();
		mso = new MoneySummaryOverview(this, new TransactionDataTable(null), dataBox);
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(AppTexts.DTF_MENU, AppTexts.DTF_MENU_ITEMS, 3);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				case "About" -> jmc.setItemAction(i, event -> about());
				case "About SufuLib" -> jmc.setItemAction(i, event -> Lib.aboutGUI(this));
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
		new ErrorWindow("About", 600, 300, "Money manager created by Riyufuchi.\nFinal version: 0.1.22\nLegacy update version: 0.5\n"
			+ "This is leagacy functionality.\nIt will not be updated anymore probably.\nThis app was ment to replace old version that used object DB (JPA), "
			+ "but funtionility become outdated, so rework was needed.");
	}
	
	// GETTERS
	
	public DataBox<MoneySum> getDataBox()
	{
		return dataBox;
	}

	@Override
	public JFrame getSelf()
	{
		return this;
	}
}
