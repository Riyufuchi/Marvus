package riyufuchi.marvus.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.dialogs.tools.categories.AddCategory;
import riyufuchi.marvus.dialogs.tools.categories.EditCategory;
import riyufuchi.marvus.dialogs.tools.macro.AddTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.DeleteTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.EditTransactionMacro;
import riyufuchi.marvus.dialogs.tools.names.AddEntityName;
import riyufuchi.marvus.dialogs.tools.names.EditEntityName;
import riyufuchi.marvus.dialogs.tools.names.RemoveEntityName;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public class EntityManagerController
{
	private JFrame parentFrame;
	private MarvusDatabase database;

	public EntityManagerController(JFrame appManager, MarvusDatabase database)
	{
		this.parentFrame = appManager;
		this.database = database;
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
	
	public void editEntityBtnEvt()
	{
		String name = new EditEntityName(parentFrame).showAndGet();
		if (name == null)
			return;
		String[] arr = name.split(" ");
		MarvusDatabase.utils.setEntityName(Integer.valueOf(arr[1]), arr[0]);
		if (saveEntityNamesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Entity name successfuly edited!", "Entity name operation");
		
	}
	
	public void removeEntityBtnEvt()
	{
		String index = new RemoveEntityName(parentFrame).showAndGet();
		if (index == null)
			return;
		MarvusDatabase.utils.removeEntityName(Integer.valueOf(index));
		if (saveEntityNamesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Entity name successfuly removed!", "Entity name operation");
		
	}
	
	public void sortEntityBtnEvt()
	{
		MarvusDatabase.utils.sortEntityNames();
		if (saveEntityNamesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Entity names successfuly sorted!", "Entity names operation");
		
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
			SufuDialogHelper.informationDialog(parentFrame, "Category list sorted!", "Category operation");
	}
	
	// Macros
	
	private boolean saveMacroToFile()
	{
		try
		{
			SufuPersistence.serializeStructure(MarvusConfig.TRANSACTION_MACRO_TABLE_PATH, database.macroTable);
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
		TransactionMacro tm = new AddTransactionMacro(parentFrame, database).showAndGet();
		if (tm == null)
			return;
		database.macroTable.add(tm.name(), tm);
		if (saveMacroToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly added!", "Transaction macro operation");
	}
	
	public void editTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new EditTransactionMacro(parentFrame, database).showAndGet();
		if (tm != null)
			if (saveMacroToFile())
				SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly edited!", "Transaction macro operation");
	}
	
	public void removeTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new DeleteTransactionMacro(parentFrame, database).showAndGet();
		if (tm != null)
		{
			if (SufuDialogHelper.booleanDialog(parentFrame, "Delete: " + tm.name(), "Are you sure?"))
			{
				database.macroTable.remove(tm.name());
				if (saveMacroToFile())
					SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly removed!", "Transaction macro operation");
			}
		}
	}
}