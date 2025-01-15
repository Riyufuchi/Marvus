package riyufuchi.marvus.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.dialogs.tools.macro.AddTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.DeleteTransactionMacro;
import riyufuchi.marvus.dialogs.tools.macro.EditTransactionMacro;
import riyufuchi.marvusLib.enums.MarvusAction;
import riyufuchi.marvusLib.gui.MarvusComboBoxDialog;
import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;
import riyufuchi.marvusLib.records.MarvusComboBoxDialogTexts;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.interfaces.SufuIDatabase;
import riyufuchi.sufuLib.records.SufuPair;
import riyufuchi.sufuLib.records.SufuRow;

/**
 * @author riyufuchi
 * @since ?
 * @version 15.01.2025
 */
public class EntityManagerController
{
	public final TargetTableInfo ENTITIES, CATEGORIES;
	private final String DEFAULT_DIALOG_TEXT;
	private final String MACRO_TITLE;
	private JFrame parentFrame;
	private MarvusDatabaseController database;
	private SufuPair<SufuRow<Integer, String>, SufuRow<Integer, String>> pair;
	
	public record TargetTableInfo(MarvusComboBoxDialogTexts texts, String path, SufuIDatabase<Integer, String> tableController, int tableID) {};
	
	public EntityManagerController(JFrame appManager, MarvusDatabaseController database)
	{
		this.DEFAULT_DIALOG_TEXT = "%s %s was %s!";
		this.MACRO_TITLE  = "Tranaction macro operation";
		this.ENTITIES = new TargetTableInfo(MarvusTexts.NAME_DIALOG_TEXT, MarvusConfig.ENTITY_TABLE_PATH, database.getEntitiesTableController(), 1);
		this.CATEGORIES = new TargetTableInfo(MarvusTexts.CATEGORY_DIALOG_TEXT, MarvusConfig.CATEGORY_TABLE_PATH, database.getCategoriesTableController(), 2);
		this.parentFrame = appManager;
		this.database = database;
		this.pair = null;
	}
	
	private <K extends Serializable, E extends Serializable> boolean saveTableToFile(String path, SufuIDatabase<K, E> table)
	{
		try
		{
			SufuPersistence.serializeStructure(path, (SufuTableDB<K, E>)table);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return false;
		}
		return true;
	}
	
	private <K extends Serializable, E extends Serializable> void tableToFile(boolean result, String title, MarvusAction action, String path, SufuIDatabase<K, E> table)
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
	
	private void actionResult(boolean result, String title, MarvusAction action)
	{
		if (result)
		{
			SufuDialogHelper.informationDialog(parentFrame, String.format(DEFAULT_DIALOG_TEXT, title, action.toString(), "successful"), title);
			return;
		}
		SufuDialogHelper.errorDialog(parentFrame, String.format(DEFAULT_DIALOG_TEXT, title, action.toString(), "unsuccessful"), title);
	}
	
	public void genericBtnEvt(TargetTableInfo info, MarvusAction userAction)
	{
		pair = new MarvusComboBoxDialog(parentFrame, info.texts, info.tableController, userAction).showAndGet();
		if (pair == null)
			return;
		switch (info.tableID)
		{
			case 1 -> entityMethodSwitch(info, userAction);
			case 2 -> categoryMethodSwitch(info, userAction);
			default -> {}
		}
	}
	
	private void entityMethodSwitch(TargetTableInfo info, MarvusAction action)
	{
		switch (action)
		{
			case ADD -> actionResult(database.insertEntity(pair.item().entity()), info.texts.title(), action);
			case EDIT -> actionResult(database.updateEntity(pair.index().id(), pair.item().entity()), info.texts.title(), action);
			case DELETE -> actionResult(database.removeEntity(pair.index().id(), pair.item().id()), info.texts.title(), action);
			default -> {}
		}
	}
	
	private void categoryMethodSwitch(TargetTableInfo info, MarvusAction action)
	{
		switch (action)
		{
			case ADD -> actionResult(database.insertCategory(pair.item().entity()), info.texts.title(), action);
			case EDIT -> actionResult(database.updateCategory(pair.index().id(), pair.item().entity()), info.texts.title(), action);
			case DELETE -> actionResult(database.removeCategory(pair.index().id(), pair.item().id()), info.texts.title(), action);
			default -> {}
		}
	}
	
	// Macros
	
	public void addTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new AddTransactionMacro(parentFrame, database).showAndGet();
		if (tm != null)
			tableToFile(database.getMacrosTableController().add(tm.name(), tm),
					MACRO_TITLE, MarvusAction.ADD, MarvusConfig.TRANSACTION_MACRO_TABLE_PATH, database.getMacrosTableController());
	}
	
	public void editTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new EditTransactionMacro(parentFrame, database).showAndGet();
		if (tm != null)
			tableToFile(database.getMacrosTableController().set(tm.name(), tm),
					MACRO_TITLE, MarvusAction.EDIT, MarvusConfig.TRANSACTION_MACRO_TABLE_PATH, database.getMacrosTableController());
	}
	
	public void removeTransactionMacroBtnEvt()
	{
		TransactionMacro tm = new DeleteTransactionMacro(parentFrame, database).showAndGet();
		if (tm != null && SufuDialogHelper.booleanDialog(parentFrame, "Delete macro for " + tm.name(), "Are you sure?"))
			tableToFile(database.getMacrosTableController().delete(tm.name()),
				MACRO_TITLE, MarvusAction.DELETE, MarvusConfig.TRANSACTION_MACRO_TABLE_PATH, database.getMacrosTableController());
	}
}
