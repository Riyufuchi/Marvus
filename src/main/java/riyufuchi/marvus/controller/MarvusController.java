package riyufuchi.marvus.controller;

import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.dialogs.AddDialog;
import riyufuchi.marvus.dialogs.TransactionIO;
import riyufuchi.marvus.tabs.CategorizedMonthListTab;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.IMarvusController;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.marvusLib.records.FileInput;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuGridPane;

/**
 * @author Riyufuchi
 * @since 25.12.2023
 * @version 06.09.2024
 */
public class MarvusController implements IMarvusController, MarvusTabbedFrame
{
	private MarvusDatabase database;
	private final MarvusDataWindow controledWindow;
	private DataDisplayTab currentMode, prevMode, dummyMode;
	private SufuGridPane panel;
	private int financialYear;
	
	public MarvusController(MarvusDataWindow controledWindow)
	{
		this.database = new MarvusDatabase(e -> SufuDialogHelper.errorDialog(controledWindow, e, "Marvus database error"));
		this.controledWindow = controledWindow;
		this.panel = new SufuGridPane();
		this.currentMode = new CategorizedMonthListTab(this);
		this.prevMode = currentMode;
		setFinancialYear(LocalDate.now().getYear());
	}
	
	public void addNewTransaction()
	{
		new AddDialog(controledWindow).showDialog();
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
		if(database.isEmpty())
		{
			SufuDialogHelper.warningDialog(controledWindow, "No data to backup", "Backup error");
			return;
		}
		if(SufuDialogHelper.yesNoDialog(controledWindow, "Are you sure?", "Data backup") == 1)
			return;
		String path = MarvusConfig.workFolder + "backups/" + LocalDate.now() + "/";
		try
		{
			if(SufuFileHelper.checkDirectory(path))
			{
				if(SufuDialogHelper.yesNoDialog(controledWindow, "Are you really sure?\nThis action will overwrite existing backups.", "Backup creation") == 1)
					return;
			}
			else
			{
				SufuDialogHelper.informationDialog(controledWindow, ("Created directory: " + path), "Backup directory created");
			}
			SufuPersistence.<Transaction>saveToCSV(path + "data.csv", database);
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
		if (MarvusConfig.currentWorkFile != null)
			MarvusIO.quickSave(controledWindow.getSelf(), MarvusConfig.currentWorkFile.getAbsolutePath(), database);
		else
			SufuDialogHelper.warningDialog(controledWindow.getSelf(), "No save destination found!", "No save destination");
	}
	
	public void saveFile()
	{
		if (MarvusConfig.currentWorkFile == null)
		{
			SufuDialogHelper.warningDialog(controledWindow.getSelf(), "No save destination found!", "No save destination");
			return;
		}
		try
		{
			MarvusIO.saveData(controledWindow.getSelf(), MarvusConfig.currentWorkFile.getAbsolutePath(), database, false);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(controledWindow.getSelf(), e);
		}
	}
	
	public void exportData()
	{
		if (isOperationUnexucatable())
			return;
		TransactionIO fio = MarvusGuiUtils.createTransactionIO(controledWindow);
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	public void importData()
	{
		File newFile = MarvusGuiUtils.createTransactionIO(controledWindow).showLoadChooser();
		if (!controledWindow.containsTabNamed(newFile.getName()))
		{
			MarvusConfig.currentWorkFile = newFile;
			MarvusController newCon = new MarvusController(controledWindow);
			controledWindow.newTab(newCon);
			newCon.quickOpenFile();
		}
	}

	public void quickOpenFile()
	{
		if (MarvusConfig.currentWorkFile == null)
		{
			importData();
			return;
		}
		FileInput fi = null;
		try
		{
			SufuFileHelper.checkFile(MarvusConfig.currentWorkFile.getAbsolutePath());
			fi = MarvusIO.inputFile(MarvusConfig.currentWorkFile.getAbsolutePath());
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(controledWindow, e);
			return;
		}
		fi.setDataTo(this);
		controledWindow.renameTab(MarvusConfig.currentWorkFile.getName());
		if(!database.isEmpty())
		{
			MarvusConfig.currentFinancialYear = database.getByID(1).get().getDate().getYear();
			financialYear = MarvusConfig.currentFinancialYear;
		}
		displayData();
	}
	
	public void switchDataDisplayMode()
	{
		dummyMode = currentMode;
		if (currentMode.parentTab() == null)
			currentMode = prevMode;
		else
			currentMode = currentMode.parentTab();
		prevMode = dummyMode;
		displayData();
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
		currentMode.refresh();
		controledWindow.refreshWindow();
	}

	@Override
	public void displayData()
	{
		panel.removeAll(); // Removes all previous content
		currentMode.prepareUI(); // Prepares static content such as menus
		currentMode.displayData(); // Displays/ prepares data to by displayed
		controledWindow.refreshWindow();
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
		currentMode.setNewData(database); // current mode might have still old data table
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
		MarvusConfig.currentFinancialYear = financialYear;
	}
	
	// GETTERS
	
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
		return panel.getGBC(x, y);
	}

	@Override
	public SufuGridPane getPane()
	{
		return panel;
	}

	@Override
	public MarvusController getController()
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
}
