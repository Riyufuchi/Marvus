package riyufuchi.marvus.controller;

import java.io.IOException;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.database.MarvusConnection;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.dialogs.tools.categories.AddCategory;
import riyufuchi.marvus.dialogs.tools.categories.EditCategory;
import riyufuchi.marvus.dialogs.tools.macro.AddTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.DeleteTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.EditTransactionMacro;
import riyufuchi.marvus.dialogs.tools.names.AddEntityName;
import riyufuchi.marvus.dialogs.tools.names.EditEntityName;
import riyufuchi.marvus.dialogs.tools.names.RemoveEntityName;
import riyufuchi.marvusLib.database.MarvusDatabaseTable;
import riyufuchi.marvusLib.enums.UserAction;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.records.SufuPair;
import riyufuchi.sufuLib.records.SufuSimpleRow;

public class EntityManagerController
{
	private final String ENTITY_DIALOG_TITLE;
	private final String DEFAULT_DIALOG_TEXT;
	private JFrame parentFrame;
	private MarvusDatabase database;

	public EntityManagerController(JFrame appManager, MarvusDatabase database)
	{
		this.ENTITY_DIALOG_TITLE = "Entity name operation";
		this.DEFAULT_DIALOG_TEXT = "%s %s was %s!";
		this.parentFrame = appManager;
		this.database = database;
	}
	
	private boolean saveTableToFile(String path, MarvusDatabaseTable<String> table)
	{
		try
		{
			SufuPersistence.serializeStructure(path, table);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return false;
		}
		return true;
	}
	
	private void tableToFile(boolean result, String title, UserAction action, String path, MarvusDatabaseTable<String> table)
	{
		if (result)
		{
			if (saveTableToFile(path, table))
			{
				SufuDialogHelper.informationDialog(parentFrame, String.format(DEFAULT_DIALOG_TEXT, title, action.toString(), "successful"), title);
				return;
			}
		}
		SufuDialogHelper.errorDialog(parentFrame, String.format(DEFAULT_DIALOG_TEXT, title, action.toString(), "unsuccessful"), title);
	}
	
	// Names
	
	public void addEntityBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair = new AddEntityName(parentFrame, database).showAndGet();
		if (pair == null)
			return;
		tableToFile(database.getEntitiesTableController().add(pair.item().entity()),
			ENTITY_DIALOG_TITLE, UserAction.ADD,
				MarvusConfig.ENTITY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getEntitiesTableController());
	}
	
	public void editEntityBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair = new EditEntityName(parentFrame, database).showAndGet();
		if (pair == null)
			return;
		boolean result = new MarvusConnection(database)
			.updateAtribbute("Name", pair.index().entity(), pair.item().entity());
		result = result && database.getEntitiesTableController().set(pair.index().id(), pair.item().entity());
		tableToFile(result, ENTITY_DIALOG_TITLE, UserAction.EDIT,
					MarvusConfig.ENTITY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getEntitiesTableController());
	}
	
	public void removeEntityBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair = new RemoveEntityName(parentFrame, database).showAndGet();
		if (pair == null)
			return;
		tableToFile(database.removeEntity(pair.index().id(), pair.item().id()),
			ENTITY_DIALOG_TITLE, UserAction.DELETE,
				MarvusConfig.ENTITY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getEntitiesTableController());
	}
	
	// Category
	
	public void addCategoryBtnEvt()
	{
		SufuSimpleRow<String> categories = new AddCategory(parentFrame, database).showAndGet();
		if (categories == null)
			return;
		database.getCategoriesTableController().add(categories.entity());
		if (saveTableToFile(MarvusConfig.CATEGORY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getCategoriesTableController()))
			SufuDialogHelper.informationDialog(parentFrame, "Category successfuly added!", "Category operation");
		
	}
	
	public void editCategoryBtnEvt()
	{
		SufuSimpleRow<String> categories = new EditCategory(parentFrame, database).showAndGet();
		if (categories == null)
			return;
		database.getCategoriesTableController().set(categories.id(), categories.entity());
		if (saveTableToFile(MarvusConfig.CATEGORY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getCategoriesTableController()))
			SufuDialogHelper.informationDialog(parentFrame, "Category successfuly edited!", "Category operation");
	}
	
	public void removeCategoryBtnEvt()
	{
		SufuSimpleRow<String> category = new EditCategory(parentFrame, database).showAndGet();
		if (category == null)
			return;
		if (SufuDialogHelper.booleanDialog(parentFrame, "Delete: " + category.entity(), "Are you sure?"))
		{
			database.getCategoriesTableController().remove(category.id());
			if (saveTableToFile(MarvusConfig.CATEGORY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getCategoriesTableController()))
				SufuDialogHelper.informationDialog(parentFrame, "Category successfuly removed!", "Category operation");
		}
	}
	
	// Macros
	
	private boolean saveMacroToFile()
	{
		try
		{
			SufuPersistence.serializeStructure(MarvusConfig.TRANSACTION_MACRO_TABLE_PATH, (SufuTableDB<String, TransactionMacro>) database.getMacrosTableController());
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
		database.getMacrosTableController().add(tm.name(), tm);
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
				database.getMacrosTableController().remove(tm.name());
				if (saveMacroToFile())
					SufuDialogHelper.informationDialog(parentFrame, "Macro successfuly removed!", "Transaction macro operation");
			}
		}
	}
}
