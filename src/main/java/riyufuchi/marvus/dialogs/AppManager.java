package riyufuchi.marvus.dialogs;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @version 1.2 - 12.10.2023
 * @since 07.10.2023
 */
public class AppManager extends SufuDialog
{
	private JButton addCategoryBtn, editCategoryBtn, removeCategoryBtn, sortCategoriesBtn;
	private JButton addMacroBtn, editMacroBtn, removeMacroBtn, sortMacroBtn;
	
	public AppManager(JFrame parentFrame)
	{
		super("Application manager", parentFrame, DialogType.OK);
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		addCategoryBtn = SufuFactory.newButton("Add category", evt -> addCategoryBtnEvt());
		editCategoryBtn = SufuFactory.newButton("Edit category", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		removeCategoryBtn = SufuFactory.newButton("Remove category", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		sortCategoriesBtn = SufuFactory.newButton("Sort categories", evt -> sortCategories());
		
		addMacroBtn = SufuFactory.newButton("Add macro", evt -> addTransactionMacroBtnEvt());
		editMacroBtn = SufuFactory.newButton("Edit macro", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		removeMacroBtn = SufuFactory.newButton("Remove macro", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		sortMacroBtn = SufuFactory.newButton("Sort macro", evt -> sortTransactionMacro());
		
		SufuGuiTools.addComponents(this, 0, 0, addCategoryBtn, editCategoryBtn, removeCategoryBtn, sortCategoriesBtn);
		SufuGuiTools.addComponents(this, 1, 0, addMacroBtn, editMacroBtn, removeMacroBtn, sortMacroBtn);
	}
	
	private void addCatagory() throws NullPointerException, IOException
	{
		String[] categories = new AddCategory(parentFrame).showAndGet();
		if (categories == null)
			return;
		LinkedList<String> list = SufuPersistence.loadTextFile(MarvusConfig.CATEGORY_FILE_PATH);
		for (String s : categories)
			list.add(s);
		MarvusDatabase.utils.setCategory(categories);
		SufuPersistence.saveToCSVtoString(MarvusConfig.CATEGORY_FILE_PATH, list);
	}
	
	private void addCategoryBtnEvt()
	{
		try
		{
			addCatagory();
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
		}
	}
	
	private void sortCategories()
	{
		Arrays.sort(MarvusDatabase.utils.getCategoryList());
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.CATEGORY_FILE_PATH, MarvusDatabase.utils.getCategoryList());
		} 
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		SufuDialogHelper.informationDialog(parentFrame, "Done!", "Category sort");
	}
	
	
	private void addTransactionMacro() throws NullPointerException, IOException
	{
		TransactionMacro tm = new AddTransactionMacro(parentFrame).showAndGet();
		if (tm == null)
			return;
		LinkedList<String> list = SufuPersistence.loadTextFile(MarvusConfig.TRANSACTION_MACRO_FILE_PATH);
		list.add(tm.toCSV());
		MarvusDatabase.utils.setMacro(tm);
		SufuPersistence.saveToCSVtoString(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, list);
	}	
	
	private void addTransactionMacroBtnEvt()
	{
		try
		{
			addTransactionMacro();
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
		}
	}
	
	private void sortTransactionMacro()
	{
		LinkedList<TransactionMacro> list = new LinkedList<>();
		for (int i= 0; i < MarvusDatabase.utils.getNames().length; i++)
			list.add(MarvusDatabase.utils.getMacro(i));
		Comparator<TransactionMacro> comp = (m1, m2) -> m1.name().compareTo(m2.name());
		list.sort(comp);
		try
		{
			SufuPersistence.<TransactionMacro>saveToCSV(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, list);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		MarvusDatabase.utils.initialize(); //Reloads macros
		SufuDialogHelper.informationDialog(parentFrame, "Done!", "Transaction macro sort");
	}

	@Override
	protected void onOK()
	{
		
	}
}
