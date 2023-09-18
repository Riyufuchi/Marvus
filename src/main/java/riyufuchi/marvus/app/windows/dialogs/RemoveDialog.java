package riyufuchi.marvus.app.windows.dialogs;

import javax.swing.JFrame;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * Dialog for removing transactions
 * 
 * Created On: 16.05.2023<br>
 * Last Edit: 24.08.2023 <br>
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
			MarvusDataWindow budgetDataTable = (MarvusDataWindow)parentFrame;
			budgetDataTable.getTable().remove(getTransaction());
			budgetDataTable.refresh();
		}
	}
}
