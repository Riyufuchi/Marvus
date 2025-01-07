package riyufuchi.marvus.controller;

import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.database.MarvusConnection;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.dialogs.io.TransactionIO;
import riyufuchi.marvus.dialogs.transactions.AddTransactionDialog;
import riyufuchi.marvus.interfaces.IMarvusController;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.records.FileInput;
import riyufuchi.marvus.tabs.CategorizedMonthListTab;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.marvus.tabs.DataSummaryTab;
import riyufuchi.marvus.tabs.TableTab;
import riyufuchi.marvus.tabs.TimedDetailTab;
import riyufuchi.marvus.tabs.UncategorizedMonthListTab;
import riyufuchi.marvus.tabs.YearOverviewTab;
import riyufuchi.marvus.tabs.YearSummaryTab;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvus.utils.MarvusIO;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvusLib.enums.UserAction;
import riyufuchi.marvusLib.records.LastChange;
import riyufuchi.sufuLib.files.SufuFileHelper;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.interfaces.SufuTab;

/**
 * @author Riyufuchi
 * @since 25.12.2023
 * @version 07.01.2025
 */
public class TabController implements IMarvusController, MarvusTabbedFrame, SufuTab
{
	private MarvusDatabase database;
	private final MarvusDataWindow controledWindow;
	private DataDisplayTab currentMode, prevMode, dummyMode;
	private DataDisplayTab[] subTabs;
	private int financialYear;
	private File currentWorkFile;
	private boolean quickOpened;
	private LastChange lastAction;
	
	public TabController(MarvusDataWindow controledWindow)
	{
		this(controledWindow, MarvusConfig.DEFAULT_WORK_FILE);
	}
	
	public TabController(MarvusDataWindow controledWindow, File file)
	{
		this.database = new MarvusDatabase(e -> SufuDialogHelper.errorDialog(controledWindow, e, "Marvus database error"), controledWindow);
		this.controledWindow = controledWindow;
		this.subTabs = new DataDisplayTab[7]; // Num of tabs in riyufuchi.marvus.tabs package
		this.currentMode = subTabs[0] = new TableTab(this);
		this.prevMode = currentMode;
		this.currentWorkFile = file;
		this.quickOpened = false;
		this.lastAction = new LastChange(UserAction.NONE, null);
		setFinancialYear(LocalDate.now().getYear());
	}
	
	public void executeQuarry()
	{
		MarvusConnection con = new MarvusConnection(database);
		if (con.updateAtribbute("Name", "Vinotéka", "Category", "Fun", "Alcohol"))
		{
			SufuDialogHelper.informationDialog(controledWindow, "Success!", "SQL result");
			refresh();
		}
	}
	
	public void saveChanges()
	{
		if (lastAction.userAction() != UserAction.NONE && lastAction.transactionInQuestion() != null)
			if (SufuDialogHelper.booleanDialog(controledWindow, "Last action: " + lastAction.userAction().toString() + "\n" + lastAction.transactionInQuestion().toString(),
					"Save unsaved actions?"))
				saveDataToFile();
	}
	
	public void addNewTransaction()
	{
		new AddTransactionDialog(this, database).showDialog();
		refresh();
	}
	
	public void sortData()
	{
		var result = SufuDialogHelper.<CompareMethod>optionDialog(controledWindow, "Choose sorting method", "Sorting method chooser", CompareMethod.values());
		database.sortData(TransactionComparation.compareBy(result));
		refresh();
	}
	
	public void createBackup()
	{
		database.createBackup();
		if(database.isEmpty())
		{
			SufuDialogHelper.warningDialog(controledWindow, "No data to backup", "Backup error");
			return;
		}
		if(!SufuDialogHelper.booleanDialog(controledWindow, "Are you sure?", "Data backup"))
			return;
		String path = currentWorkFile.getParentFile().getAbsolutePath() + "/backups/" + LocalDate.now() + "/";
		try
		{
			if(SufuFileHelper.checkDirectory(path))
			{
				if(!SufuDialogHelper.booleanDialog(controledWindow, "Are you really sure?\nThis action will overwrite existing backups.", "Backup creation"))
					return;
			}
			else
			{
				SufuDialogHelper.informationDialog(controledWindow, ("Created directory: " + path), "Backup directory created");
			}
			SufuPersistence.<Transaction>saveToCSV(path + currentWorkFile.getName(), database);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(controledWindow.getSelf(), e);
			return;
		}
		SufuDialogHelper.informationDialog(controledWindow, "Backup successfuly created", "Task successful");
	}
	
	public void quickSaveFile()
	{
		if (isOperationUnexucatable()) // Prevents accidental data deletion
			return;
		if (currentWorkFile != null)
			MarvusIO.quickSave(controledWindow.getSelf(), currentWorkFile.getAbsolutePath(), database);
		else
			SufuDialogHelper.warningDialog(controledWindow.getSelf(), "No save destination found!", "No save destination");
		setLastAction(new LastChange(UserAction.NONE, null));
	}
	
	public void saveDataToFile()
	{
		if (isOperationUnexucatable()) // Prevents accidental data deletion
			return;
		if (currentWorkFile == null)
		{
			SufuDialogHelper.warningDialog(controledWindow.getSelf(), "No save destination found!", "No save destination");
			return;
		}
		try
		{
			MarvusIO.saveData(controledWindow.getSelf(), currentWorkFile.getAbsolutePath(), database, false);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(controledWindow.getSelf(), e);
		}
		setLastAction(new LastChange(UserAction.NONE, null));
	}
	
	public void exportData()
	{
		if (isOperationUnexucatable())
			return;
		TransactionIO fio = MarvusGuiUtils.createTransactionIO(controledWindow);
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	public boolean importData()
	{
		return MarvusGuiUtils.createTransactionIO(controledWindow).showLoadChooser();
	}

	public boolean quickOpenFile()
	{
		if (currentWorkFile == null || quickOpened)
		{
			return importData();
		}
		FileInput fi = null;
		try
		{
			SufuFileHelper.checkFile(currentWorkFile.getAbsolutePath());
			fi = MarvusIO.inputFile(currentWorkFile.getAbsolutePath());
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(controledWindow, e);
			return false;
		}
		fi.setDataTo(this);
		controledWindow.renameTab(currentWorkFile.getName());
		database.getByID(1).ifPresent(transaction -> financialYear = transaction.getDate().getYear());
		displayData();
		quickOpened = true;
		return true;
	}
	
	public void updateDataDisplayMode(int id)
	{
		if (subTabs[id] == null)
			updateDataDisplayMode(subTabs[id] = updateSubTab(id));
		else
			updateDataDisplayMode(subTabs[id]);
	}
	
	private DataDisplayTab updateSubTab(int id)
	{
		switch(id)
		{
			case 0 -> { return new TableTab(this); }
			case 1 -> { return new CategorizedMonthListTab(this); }
			case 2 -> { return new UncategorizedMonthListTab(this); }
			case 3 -> { return new YearSummaryTab(this, financialYear); }
			case 4 -> { return new YearOverviewTab(this, financialYear); }
			case 5 -> { return new DataSummaryTab(this, financialYear); }
			case 6 -> { return new TimedDetailTab(this); }
			default -> { return new DataSummaryTab(this, financialYear); }
		}
	}
	
	public void switchDataDisplayMode()
	{
		dummyMode = currentMode;
		if (currentMode.getParentTab() == null)
			currentMode = prevMode;
		else
			currentMode = currentMode.getParentTab();
		prevMode = dummyMode;
		refresh();
	}
	
	@Override
	public void updateDataDisplayMode(DataDisplayTab ddm)
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
		if (!isOperationUnexucatable())
			displayData();
	}

	@Override
	public void refresh()
	{
		if (isOperationUnexucatable())
			return;
		controledWindow.setPane(currentMode.getTabsPanel());
		currentMode.refresh();
		controledWindow.refreshWindow();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void displayData()
	{
		controledWindow.setPane(currentMode.getTabsPanel());
		currentMode.prepareUI();
		currentMode.displayData(); // Displays data
		controledWindow.refreshWindow();
	}
	
	// On methods
	
	@Override
	public boolean onClose()
	{
		saveChanges();
		return SufuDialogHelper.booleanDialog(controledWindow, ("Close tab " + controledWindow.getCurrentTabName() + "?"), "Tab action");
	}
	
	// Is methods
	
	/**
	 * 
	 * @return true when operation can't be executed
	 */
	public boolean isOperationUnexucatable()
	{
		if(database.isEmpty())
		{
			SufuDialogHelper.warningDialog(controledWindow.getSelf(), "No data to work with!", "No data found");
			return true;
		}
		return false;
	}
	
	// SETTERS
	
	public void setWorkFile(File file)
	{
		this.currentWorkFile = file;
	}

	public void setCurrentTab(DataDisplayTab tab)
	{
		currentMode = tab;
	}
	
	@Override
	public void setDataDisplayMode(DataDisplayTab ddm)
	{
		if (ddm == null)
			return;
		setDatabase(ddm.getDataSource());
		ddm.setTargetWindow(this); // ddm have reference to old window
		updateDataDisplayMode(ddm);
	}
	
	@Override
	public void setDatabase(MarvusDatabase database)
	{
		this.database = database;
		currentMode.setNewData(this.database);
		prevMode.setNewData(this.database);
		// Because if database is loaded from serialization, comparator and errorHandler will be null
		this.database.setErrorHandler(s -> SufuDialogHelper.warningDialog(controledWindow, s, "Data error"));
		this.database.setComparator(TransactionComparation.compareFC(CompareMethod.By_name));
	}
	
	public void setFinancialYear(int financialYear)
	{
		this.financialYear = financialYear;
	}
	
	public void setLastAction(LastChange ls)
	{
		this.lastAction = ls;
	}
	

	
	// GETTERS
	
	public File getWorkFile()
	{
		return currentWorkFile;
	}
	
	public int getFinancialYear()
	{
		return financialYear;
	}
	
	public MarvusDatabase getDatabase()
	{
		return database;
	}
	
	@Override
	public GridBagConstraints getGBC(int x, int y)
	{
		return currentMode.getTabsPanel().getGBC(x, y);
	}

	@Override
	public JPanel getPane()
	{
		return currentMode.getTabsPanel();
	}

	@Override
	public TabController getController()
	{
		return this;
	}
	
	@Override
	public DataDisplayTab getCurrentTab()
	{
		return currentMode;
	}

	@Override
	public DataDisplayTab getPreviousTab()
	{
		return prevMode;
	}

	@Override
	public JFrame getSelf()
	{
		return controledWindow;
	}

	@SuppressWarnings("null")
	@Override
	public void setPane(JPanel pane)
	{
		// This is breaker code as this override is result of "spaghetti code"s
		DataDisplayTab d = null;
		d.getParentTab();
		//this.currentMode.set = pane;
	}
}
