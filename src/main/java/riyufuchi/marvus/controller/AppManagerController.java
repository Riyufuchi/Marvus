package riyufuchi.marvus.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.dialogs.tools.AddCategory;
import riyufuchi.marvus.dialogs.tools.AddTransactionMacro;
import riyufuchi.marvus.dialogs.tools.DeleteTransactionMacro;
import riyufuchi.marvus.dialogs.tools.EditTransactionMacro;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public class AppManagerController
{
	private JFrame parentFrame;

	public AppManagerController(JFrame appManager)
	{
		this.parentFrame = appManager;
	}
	
	// Category
	
	public void addCategoryBtnEvt()
	{
		String[] categories = new AddCategory(parentFrame).showAndGet();
		if (categories == null)
			return;
		MarvusDatabase.utils.setCategory(categories);
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.CATEGORY_FILE_PATH, MarvusDatabase.utils.getCategoryEnum());
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
		}
	}
	
	public void sortCategories()
	{
		Arrays.sort(MarvusDatabase.utils.getCategoryEnum());
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.CATEGORY_FILE_PATH, MarvusDatabase.utils.getCategoryEnum());
		} 
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		SufuDialogHelper.informationDialog(parentFrame, "Category list sorted!", "Sorting result");
	}
	
	// Macros
	
	private boolean saveMacroToFile()
	{
		try
		{
			SufuPersistence.saveToCSVtoString(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, MarvusDatabase.utils.getTransactionMacroCSV());
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return false;
		}
		return true;
	}
	
	public void addTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new AddTransactionMacro(parentFrame).showAndGet();
		if (tm == null)
			return;
		MarvusDatabase.utils.addMacro(tm);
		if (saveMacroToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly added!", "Transaction macro operation");
	}
	
	public void editTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new EditTransactionMacro(parentFrame).showAndGet();
		if (tm != null)
			if (saveMacroToFile())
				SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly edited!", "Transaction macro operation");
	}
	
	public void deleteTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new DeleteTransactionMacro(parentFrame).showAndGet();
		if (tm != null)
		{
			if (SufuDialogHelper.booleanDialog(parentFrame, "Delete: " + tm.toCSV(), "Are you sure?"))
			{
				MarvusDatabase.utils.removeMacro(tm);
				if (saveMacroToFile())
					SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly removed!", "Transaction macro operation");
			}
		}
	}
	
	public void sortTransactionMacro()
	{
		LinkedList<TransactionMacro> list = new LinkedList<>();
		for (int i= 0; i < MarvusDatabase.utils.getNames().length; i++)
			list.add(MarvusDatabase.utils.getMacro(i));
		list.sort((m1, m2) -> m1.name().compareTo(m2.name()));
		try
		{
			SufuPersistence.<TransactionMacro>saveToCSV(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, list);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		MarvusDatabase.utils.initialize(); // Reloads macros
		SufuDialogHelper.informationDialog(parentFrame, "Macro list sorted!", "Sorting result");
	}
}
