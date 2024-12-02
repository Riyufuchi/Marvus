package riyufuchi.marvus.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.dialogs.tools.categories.AddCategory;
import riyufuchi.marvus.dialogs.tools.categories.EditCategory;
import riyufuchi.marvus.dialogs.tools.macro.AddTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.DeleteTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.EditTransactionMacro;
import riyufuchi.marvus.dialogs.tools.names.AddEntityName;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public class EntityManagerController
{
	private JFrame parentFrame;

	public EntityManagerController(JFrame appManager)
	{
		this.parentFrame = appManager;
	}
	
	// Names
	
	public boolean saveEntityNamesToFile()
	{
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.NAME_FILE_PATH, MarvusDatabase.utils.getEntityNamesEnum());
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return false;
		}
		return true;
	}
	
	public void addEntityBtnEvt()
	{
		String name = new AddEntityName(parentFrame).showAndGet();
		if (name == null)
			return;
		MarvusDatabase.utils.addEntityName(name);
		if (saveEntityNamesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Entity name successfuly added!", "Entity name operation");
		
	}
	
	// Category
	
	public boolean saveCategoriesToFile()
	{
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.CATEGORY_FILE_PATH, MarvusDatabase.utils.getCategoryEnum());
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return false;
		}
		return true;
	}
	
	public void addCategoryBtnEvt()
	{
		String[] categories = new AddCategory(parentFrame).showAndGet();
		if (categories == null)
			return;
		MarvusDatabase.utils.addCategory(categories);
		if (saveCategoriesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Category successfuly added!", "Category operation");
		
	}
	
	public void editCategoryBtnEvt()
	{
		String[] categories = new EditCategory(parentFrame).showAndGet();
		if (categories == null)
			return;
		MarvusDatabase.utils.setCategory(categories[0], categories[1]);
		if (saveCategoriesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Category successfuly edited!", "Category operation");
	}
	
	public void sortCategories()
	{
		Arrays.sort(MarvusDatabase.utils.getCategoryEnum());
		if (saveCategoriesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Category list sorted!", "Sorting result");
	}
	
	// Macros
	
	private boolean saveMacroToFile()
	{
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, MarvusDatabase.utils.getTransactionMacros());
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
		ArrayList<TransactionMacro> list = MarvusDatabase.utils.getTransactionMacros();
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
