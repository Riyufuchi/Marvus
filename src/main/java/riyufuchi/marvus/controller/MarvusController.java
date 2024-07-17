package riyufuchi.marvus.controller;

import java.io.IOException;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvus.windows.MarvusDataWindow;
import riyufuchi.marvus.windows.TransactionIO;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.IMarvusController;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.marvusLib.records.FileInput;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 25.12.2023
 * @version 17.07.2024
 */
public class MarvusController implements IMarvusController
{
	private MarvusDatabase database;
	private final MarvusDataWindow controledWindow;
	
	public MarvusController(MarvusDataWindow controledWindow)
	{
		this.database = new MarvusDatabase(e -> SufuDialogHelper.errorDialog(controledWindow, e, "Marvus database error"));
		this.controledWindow = controledWindow;
	}
	
	// Utility
	
	// Functions to delegate
	
	public void createBackup()
	{
		try
		{
			MarvusDeleg.backupData(controledWindow);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(controledWindow.getSelf(), e);
		}
	}
	
	public void quickSaveFile()
	{
		if (isOperationExucatable()) // Prevents accidental data deletion
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
		if (isOperationExucatable())
			return;
		TransactionIO fio = MarvusUtils.createTransactionIO(controledWindow);
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	public void importData()
	{
		MarvusConfig.currentWorkFile =  MarvusUtils.createTransactionIO(controledWindow).showLoadChooser();
		if(!database.isEmpty())
			MarvusConfig.financialYear = database.getByID(1).get().getDate().getYear();
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
		controledWindow.displayData();
	}
	
	// Is methods
	
	/**
	 * 
	 * @return true when operation can't be executed
	 */
	public boolean isOperationExucatable()
	{
		if(database.isEmpty())
		{
			SufuDialogHelper.warningDialog(controledWindow.getSelf(), "No data to work with!", "No data found");
			return true;
		}
		return false;
	}
	
	public MarvusDatabase getDatabase()
	{
		return database;
	}

	@Override
	public void setDatabase(MarvusDatabase md)
	{
		this.database = md;
	}

}
