package riyufuchi.marvus.dialogs.tools.other;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.controller.EntityManagerController;
import riyufuchi.sufuLib.gui.SufuDialogGeneric;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuGridPane;
import riyufuchi.sufuLib.gui.utils.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 07.10.2023
 * @version 10.01.2025
 */
public class EntityManagerDialog extends SufuDialogGeneric<MarvusDataWindow>
{
	private JButton addCategoryBtn, editCategoryBtn, removeCategoryBtn;
	private JButton addMacroBtn, editMacroBtn, removeMacroBtn;
	private JButton addEntityBtn, editEntityBtn, removeEntityBtn;
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
		
		addCategoryBtn = SufuFactory.newButton("Add category", evt -> controller.addCategoryBtnEvt());
		editCategoryBtn = SufuFactory.newButton("Edit category", evt -> controller.editCategoryBtnEvt());
		removeCategoryBtn = SufuFactory.newButton("Remove category", evt -> controller.removeCategoryBtnEvt());
		
		addMacroBtn = SufuFactory.newButton("Add macro", evt -> controller.addTransactionMacroBtnEvt());
		editMacroBtn = SufuFactory.newButton("Edit macro", evt -> controller.editTransactionMacroBtnEvt());
		removeMacroBtn = SufuFactory.newButton("Remove macro", evt -> controller.removeTransactionMacroBtnEvt());
		
		SufuComponentTools.disableAll(removeCategoryBtn);
		
		SufuGuiTools.addComponents(buttonPane, 0, 0, addEntityBtn, editEntityBtn, removeEntityBtn);
		SufuGuiTools.addComponents(buttonPane, 1, 0, addCategoryBtn, editCategoryBtn, removeCategoryBtn);
		SufuGuiTools.addComponents(buttonPane, 2, 0, addMacroBtn, editMacroBtn, removeMacroBtn);
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
