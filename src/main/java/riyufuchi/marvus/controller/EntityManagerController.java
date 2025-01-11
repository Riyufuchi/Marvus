package riyufuchi.marvus.controller;

import java.io.IOException;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.dialogs.tools.macro.AddTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.DeleteTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.EditTransactionMacro;
import riyufuchi.marvusLib.database.MarvusDatabaseTable;
import riyufuchi.marvusLib.enums.MarvusAction;
import riyufuchi.marvusLib.gui.MarvusComboBoxDialog;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.records.SufuPair;
import riyufuchi.sufuLib.records.SufuSimpleRow;

/**
 * @author riyufuchi
 * @since ?
 * @version 11.01.2025
 */
public class EntityManagerController
{
	private final String DEFAULT_DIALOG_TEXT;
	private JFrame parentFrame;
	private MarvusDatabase database;

	public EntityManagerController(JFrame appManager, MarvusDatabase database)
	{
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
	
	private void tableToFile(boolean result, String title, MarvusAction action, String path, MarvusDatabaseTable<String> table)
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
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair = 
				new MarvusComboBoxDialog(parentFrame, MarvusTexts.NAME_DIALOG_TEXT, database.getEntitiesTableController(), MarvusAction.ADD).showAndGet();
		if (pair == null)
			return;
		tableToFile(database.getEntitiesTableController().add(pair.item().entity()),
			MarvusTexts.NAME_DIALOG_TEXT.title(), MarvusAction.ADD,
				MarvusConfig.ENTITY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getEntitiesTableController());
	}
	
	public void editEntityBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair = 
				new MarvusComboBoxDialog(parentFrame, MarvusTexts.NAME_DIALOG_TEXT, database.getEntitiesTableController(), MarvusAction.EDIT).showAndGet();
		if (pair == null)
			return;
		boolean result = database.updateAtribbute("Name", pair.index().entity(), pair.item().entity());
		result = result && database.getEntitiesTableController().set(pair.index().id(), pair.item().entity());
		tableToFile(result, MarvusTexts.NAME_DIALOG_TEXT.title(), MarvusAction.EDIT,
					MarvusConfig.ENTITY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getEntitiesTableController());
	}
	
	public void removeEntityBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair =
				new MarvusComboBoxDialog(parentFrame, MarvusTexts.NAME_DIALOG_TEXT, database.getEntitiesTableController(), MarvusAction.DELETE).showAndGet();
		if (pair == null)
			return;
		tableToFile(database.removeEntity(pair.index().id(), pair.item().id()),
			MarvusTexts.NAME_DIALOG_TEXT.title(), MarvusAction.DELETE,
				MarvusConfig.ENTITY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getEntitiesTableController());
	}
	
	// Category
	
	public void addCategoryBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair =
				new MarvusComboBoxDialog(parentFrame, MarvusTexts.CATEGORY_DIALOG_TEXT, null, MarvusAction.ADD).showAndGet();
		if (pair == null)
			return;
		tableToFile(database.getCategoriesTableController().add(pair.item().entity()),
			MarvusTexts.CATEGORY_DIALOG_TEXT.title(), MarvusAction.ADD, 
				MarvusConfig.CATEGORY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getCategoriesTableController());
	}
	
	public void editCategoryBtnEvt()
	{
		SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>> pair = new MarvusComboBoxDialog(parentFrame, MarvusTexts.CATEGORY_DIALOG_TEXT,
				database.getCategoriesTableController(), MarvusAction.EDIT).showAndGet();
		if (pair == null)
			return;
		boolean result = database.updateAtribbute("Category", pair.index().entity(), pair.item().entity());
		result = result && database.getCategoriesTableController().set(pair.index().id(), pair.item().entity());
		tableToFile(result, 
			MarvusTexts.CATEGORY_DIALOG_TEXT.title(), MarvusAction.EDIT,
				MarvusConfig.CATEGORY_TABLE_PATH, (MarvusDatabaseTable<String>) database.getCategoriesTableController());
	}
	
	public void removeCategoryBtnEvt()
	{
		SufuDialogHelper.notImplementedYetDialog(parentFrame);
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
