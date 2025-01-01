package riyufuchi.marvus.dialogs.tools.other;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.controller.EntityManagerController;
import riyufuchi.sufuLib.gui.SufuDialogGeneric;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGridPane;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 07.10.2023
 * @version 01.01.2025
 */
public class EntityManagerDialog extends SufuDialogGeneric<MarvusDataWindow>
{
	private JButton addCategoryBtn, editCategoryBtn, removeCategoryBtn, sortCategoriesBtn;
	private JButton addMacroBtn, editMacroBtn, removeMacroBtn, sortMacroBtn;
	private JButton addEntityBtn, editEntityBtn, removeEntityBtn, sortEntityBtn;
	private JTextField fc;
	private EntityManagerController controller;
	private JPanel menuPane;
	private SufuGridPane buttonPane;
	
	public EntityManagerDialog(MarvusDataWindow parentFrame)
	{
		super("Entity manager", parentFrame, DialogType.OK);
		this.controller = new EntityManagerController(parentFrame, parentFrame.getController().getDatabase());
	}

	@Override
	protected void createInputs(JPanel panel)
	{
		menuPane = SufuFactory.newFlowPane();
		buttonPane = SufuFactory.newGridPane();
		
		panel.add(menuPane, getGBC(0, 0));
		panel.add(buttonPane, getGBC(0, 1));
		
		menuPane.add(SufuFactory.newLabel("Financial year: "));
		fc = SufuFactory.newTextField(String.valueOf(parentFrame.getController().getFinancialYear()));
		menuPane.add(fc);
		
		addEntityBtn = SufuFactory.newButton("Add entity", evt -> controller.addEntityBtnEvt());
		editEntityBtn = SufuFactory.newButton("Edit entity", evt -> controller.editEntityBtnEvt());
		removeEntityBtn = SufuFactory.newButton("Remove entity", evt -> controller.removeEntityBtnEvt());
		sortEntityBtn = SufuFactory.newButton("Sort entity", null);
		
		addCategoryBtn = SufuFactory.newButton("Add category", evt -> controller.addCategoryBtnEvt());
		editCategoryBtn = SufuFactory.newButton("Edit category", evt -> controller.editCategoryBtnEvt());
		removeCategoryBtn = SufuFactory.newButton("Remove category", evt -> controller.removeCategoryBtnEvt());
		sortCategoriesBtn = SufuFactory.newButton("Sort categories", null);
		
		addMacroBtn = SufuFactory.newButton("Add macro", evt -> controller.addTransactionMacroBtnEvt());
		editMacroBtn = SufuFactory.newButton("Edit macro", evt -> controller.editTransactionMacroBtnEvt());
		removeMacroBtn = SufuFactory.newButton("Remove macro", evt -> controller.removeTransactionMacroBtnEvt());
		sortMacroBtn = SufuFactory.newButton("Sort macro", null);
		
		SufuComponentTools.disableAll(sortEntityBtn, sortMacroBtn, sortCategoriesBtn, removeCategoryBtn, removeEntityBtn);
		
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
