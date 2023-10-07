package riyufuchi.marvus.app.windows.dialogs;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusLib.dataBase.MarvusDatabase;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @version 07.10.2023
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
		addCategoryBtn = SufuFactory.newButton("Add category", null);
		editCategoryBtn = SufuFactory.newButton("Edit category", null);
		removeCategoryBtn = SufuFactory.newButton("Remove category", null);
		sortCategoriesBtn = SufuFactory.newButton("Sort categories", evt -> sortCategories());
		
		addMacroBtn = SufuFactory.newButton("Add macro", null);
		editMacroBtn = SufuFactory.newButton("Edit macro", null);
		removeMacroBtn = SufuFactory.newButton("Remove macro", null);
		sortMacroBtn = SufuFactory.newButton("Sort macro", null);
		
		SufuGuiTools.addComponents(this, 0, 0, addCategoryBtn, editCategoryBtn, removeCategoryBtn, sortCategoriesBtn);
		SufuGuiTools.addComponents(this, 1, 0, addMacroBtn, editMacroBtn, removeMacroBtn, sortMacroBtn);
	}
	
	private void sortCategories()
	{
		Arrays.sort(MarvusDatabase.utils.getCategoryList());
		try
		{
			SufuPersistence.saveToCSV(MarvusConfig.workFolder + "category.txt", MarvusDatabase.utils.getCategoryList());
		} 
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		SufuDialogHelper.informationDialog(parentFrame, "Done!", "Category sort");
	}

	@Override
	protected void onOK()
	{
		
	}
}
