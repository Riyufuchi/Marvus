package riyufuchi.marvus.controller;

import java.io.IOException;

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
import riyufuchi.marvusLib.records.Row;
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
			SufuPersistence.serializeStructure(MarvusConfig.ENTITY_TABLE_PATH, database.entities);
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
		String name = new AddEntityName(parentFrame, database).showAndGet();
		if (name == null)
			return;
		if (database.entities.add(name))
			if (saveEntityNamesToFile())
				SufuDialogHelper.informationDialog(parentFrame, "Entity name successfuly added!", "Entity name operation");
	}
	
	public void editEntityBtnEvt()
	{
		String name = new EditEntityName(parentFrame, database).showAndGet();
		if (name == null)
			return;
		String[] arr = name.split(";");
		System.out.print(arr[1]);
		if (database.entities.set(Integer.valueOf(arr[1]), arr[0]))
			if (saveEntityNamesToFile())
				SufuDialogHelper.informationDialog(parentFrame, "Entity name successfuly edited!", "Entity name operation");
	}
	
	public void removeEntityBtnEvt()
	{
		String index = new RemoveEntityName(parentFrame, database).showAndGet();
		if (index == null)
			return;
		if (database.entities.remove(Integer.valueOf(index)))
			if (saveEntityNamesToFile())
				SufuDialogHelper.informationDialog(parentFrame, "Entity name successfuly removed!", "Entity name operation");
	}
	
	// Category
	
	public boolean saveCategoriesToFile()
	{
		try
		{
			SufuPersistence.serializeStructure(MarvusConfig.CATEGORY_TABLE_PATH, database.categories);
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
		Row<String> categories = new AddCategory(parentFrame, database).showAndGet();
		if (categories == null)
			return;
		database.categories.add(categories.entity());
		if (saveCategoriesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Category successfuly added!", "Category operation");
		
	}
	
	public void editCategoryBtnEvt()
	{
		Row<String> categories = new EditCategory(parentFrame, database).showAndGet();
		if (categories == null)
			return;
		database.categories.set(categories.id(), categories.entity());
		if (saveCategoriesToFile())
			SufuDialogHelper.informationDialog(parentFrame, "Category successfuly edited!", "Category operation");
	}
	
	public void removeCategoryBtnEvt()
	{
		Row<String> category = new EditCategory(parentFrame, database).showAndGet();
		if (category == null)
			return;
		if (SufuDialogHelper.booleanDialog(parentFrame, "Delete: " + category.entity(), "Are you sure?"))
		{
			database.categories.remove(category.id());
			if (saveCategoriesToFile())
				SufuDialogHelper.informationDialog(parentFrame, "Category successfuly removed!", "Category operation");
		}
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
