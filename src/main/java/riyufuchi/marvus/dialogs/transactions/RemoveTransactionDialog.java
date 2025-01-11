package riyufuchi.marvus.dialogs.transactions;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.enums.MarvusAction;
import riyufuchi.marvusLib.records.LastChange;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;

/**
 * Dialog for removing transactions
 * 
 * @author Riyufuchi
 * @since 16.05.2023
 * @version 07.01.2025
 */
public class RemoveTransactionDialog extends EditTransactionDialog
{
	public RemoveTransactionDialog(MarvusTabbedFrame parentFrame, Transaction transaction, MarvusDatabase database)
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
			interfaceParentWindow.getController().setLastAction(new LastChange(MarvusAction.DELETE, getTransaction()));
			interfaceParentWindow.getController().getDatabase().remove(getTransaction());
			interfaceParentWindow.getController().refresh();
		}
	}
}
