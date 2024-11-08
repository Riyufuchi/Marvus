package riyufuchi.marvus.dialogs.transactions;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.gui.SufuTableTools;

public class TransactionViewer extends SufuDialog
{
	private Iterable<Transaction> transactions;
	
	public TransactionViewer(JFrame parentFrame, Iterable<Transaction> transactions)
	{
		super("Transaction viewer", parentFrame, DialogType.OK);
		this.transactions = transactions;
		displayTransactions();
		this.pack();
		this.revalidate();
		this.repaint();
	}
	
	private void displayTransactions()
	{
		int y = 1;
		for (Transaction t : transactions)
		{
			SufuTableTools.<Object>addRowHeader(this, 0, y, t.getID(), t.getName(), t.getCategory(), t.getValue(), t.getCurrency(), t.getStringDate(), t.getNote());
			y++;
		}
	}

	@Override
	protected void createInputs(JPanel pane)
	{
		SufuTableTools.addRowHeader(this, 0, 0, MarvusTexts.TRANSACTION_VIEWER_HEADER);
	}

	@Override
	protected void onOK()
	{
		
	}
}
