package riyufuchi.marvus.dialogs;

import javax.swing.JFrame;

import riyufuchi.marvus.windows.MarvusDataWindow;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * Dialog for removing transactions
 * 
 * @author Riyufuchi
 * @since 16.05.2023
 * @version 25.12.2023
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
			MarvusDataWindow budgetDataTable = (MarvusDataWindow)parentFrame;
			budgetDataTable.getDatabase().remove(getTransaction());
			budgetDataTable.refresh();
		}
	}
}
