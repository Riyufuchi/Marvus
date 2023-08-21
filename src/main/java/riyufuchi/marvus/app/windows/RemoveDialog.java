package riyufuchi.marvus.app.windows;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.DialogHelper;

/**
 * Created On: 16.05.2023<br>
 * Last Edit: 16.05.2023 <br>
 * Dialog for removing transaction
 * 
 * @author Riyufuchi
 */
public class RemoveDialog extends EditDialog
{
	public RemoveDialog(JFrame parentFrame, Transaction transaction)
	{
		super(parentFrame, transaction);
		setTitle("Remove transaction " + transaction.getID());
	}

	@Override
	protected void onOK()
	{
		if(DialogHelper.yesNoDialog(parentFrame, "Delete transaction number: " + getTransaction().getID() + "\n" + getTransaction().toString(), "Are you sure?") == 0)
		{
			BudgetDataTable budgetDataTable = (BudgetDataTable)parentFrame;
			budgetDataTable.getDataBox().getList().remove(getTransaction().getID() - 1); // ID starts from 1
			budgetDataTable.refresh();
		}
	}
}
