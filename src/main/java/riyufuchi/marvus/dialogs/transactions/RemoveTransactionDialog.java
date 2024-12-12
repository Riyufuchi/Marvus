package riyufuchi.marvus.dialogs.transactions;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.enums.UserAction;
import riyufuchi.marvusLib.records.LastChange;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * Dialog for removing transactions
 * 
 * @author Riyufuchi
 * @since 16.05.2023
 * @version 12.12.2024
 */
public class RemoveTransactionDialog extends EditTransactionDialog
{
	public RemoveTransactionDialog(JFrame parentFrame, Transaction transaction, MarvusDatabase database)
	{
		super(parentFrame, transaction, database);
		setTitle("Remove transaction " + transaction.getID());
		pack();
	}

	@Override
	protected void onOK()
	{
		if(SufuDialogHelper.booleanDialog(parentFrame, "Delete transaction number: " + getTransaction().getID() + "\n" + getTransaction().toString(), "Are you sure?"))
		{
			MarvusDataWindow budgetDataTable = (MarvusDataWindow)parentFrame;
			budgetDataTable.getController().setLastAction(new LastChange(UserAction.DELETE, getTransaction()));
			budgetDataTable.getController().getDatabase().remove(getTransaction());
			budgetDataTable.getController().refresh();
		}
	}
}
