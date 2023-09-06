package riyufuchi.marvus.app.windows.dialogs;

import javax.swing.JFrame;

import riyufuchi.marvus.app.windows.MarvusMainWindow;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuDialogHelper;

/**
 * Created On: 16.05.2023<br>
 * Last Edit: 24.08.2023 <br>
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
		if(SufuDialogHelper.yesNoDialog(parentFrame, "Delete transaction number: " + getTransaction().getID() + "\n" + getTransaction().toString(), "Are you sure?") == 0)
		{
			MarvusMainWindow budgetDataTable = (MarvusMainWindow)parentFrame;
			budgetDataTable.getTable().remove(getTransaction());
			budgetDataTable.refresh();
		}
	}
}
