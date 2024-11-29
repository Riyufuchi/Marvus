package riyufuchi.marvus.dialogs.tools;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.controller.AppManagerController;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.sufuLib.gui.SufuDialogGeneric;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGridPane;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 07.10.2023
 * @version 29.11.2024
 */
public class AppManager extends SufuDialogGeneric<MarvusDataWindow>
{
	private JButton addCategoryBtn, editCategoryBtn, removeCategoryBtn, sortCategoriesBtn;
	private JButton addMacroBtn, editMacroBtn, removeMacroBtn, sortMacroBtn;
	private JButton addEntityBtn, editEntityBtn, removeEntityBtn, sortEntityBtn;
	private JTextField fc;
	private AppManagerController controller;
	private JPanel menuPane;
	private SufuGridPane buttonPane;
	
	public AppManager(MarvusDataWindow parentFrame)
	{
		super("Marvus manager", parentFrame, DialogType.OK);
		this.controller = new AppManagerController(parentFrame);
	}

	@Override
	protected void createInputs(JPanel panel)
	{
		menuPane = SufuFactory.newFlowPane();
		buttonPane = SufuFactory.newGridPane();
		
		panel.add(menuPane, getGBC(0, 0));
		panel.add(buttonPane, getGBC(0, 1));
		
		menuPane.add(SufuFactory.newLabel("Financial year: "));
		fc = SufuFactory.newTextField(String.valueOf(MarvusConfig.currentFinancialYear));
		//fc.setEnabled(false);
		menuPane.add(fc);
		
		addEntityBtn = SufuFactory.newButton("Add entity", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		editEntityBtn = SufuFactory.newButton("Edit entity", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		removeEntityBtn = SufuFactory.newButton("Remove entity", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		sortEntityBtn = SufuFactory.newButton("Sort entity", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		
		addCategoryBtn = SufuFactory.newButton("Add category", evt -> controller.addCategoryBtnEvt());
		editCategoryBtn = SufuFactory.newButton("Edit category", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		removeCategoryBtn = SufuFactory.newButton("Remove category", evt -> SufuDialogHelper.notImplementedYetDialog(parentFrame));
		sortCategoriesBtn = SufuFactory.newButton("Sort categories", evt -> controller.sortCategories());
		
		addMacroBtn = SufuFactory.newButton("Add macro", evt -> controller.addTransactionMacroBtnEvt());
		editMacroBtn = SufuFactory.newButton("Edit macro", evt -> controller.editTransactionMacroBtnEvt());
		removeMacroBtn = SufuFactory.newButton("Remove macro", evt -> controller.deleteTransactionMacroBtnEvt());
		sortMacroBtn = SufuFactory.newButton("Sort macro", evt -> controller.sortTransactionMacro());
		
		SufuGuiTools.addComponents(buttonPane, 0, 0, addEntityBtn, editEntityBtn, removeEntityBtn, sortEntityBtn);
		SufuGuiTools.addComponents(buttonPane, 1, 0, addCategoryBtn, editCategoryBtn, removeCategoryBtn, sortCategoriesBtn);
		SufuGuiTools.addComponents(buttonPane, 2, 0, addMacroBtn, editMacroBtn, removeMacroBtn, sortMacroBtn);
	}

	@Override
	protected void onOK()
	{
		try
		{
			parentFrame.getController().setFinancialYear(Integer.valueOf(fc.getText()));
		}
		catch (NumberFormatException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
		}
	}
}
