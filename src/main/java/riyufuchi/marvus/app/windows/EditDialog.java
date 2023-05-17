package riyufuchi.marvus.app.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.marvusData.Transaction;

/**
 * Created On: 11.05.2023<br>
 * Last Edit: 17.05.2023<br>
 * Dialog for editing Transaction
 *
 * @author Riyufuchi
 */
public class EditDialog extends AddDialog
{
	private Transaction transaction;
	
	public EditDialog(JFrame parentFrame, Transaction transaction)
	{
		super(parentFrame);
		this.transaction = transaction;
		prefillComponents();
		setTitle("Edit transaction " + transaction.getID());
	}

	private void prefillComponents()
	{
		category.setSelectedItem(transaction.getName());
		name.setText(transaction.getName());
		money.setText(transaction.getMoneySum().toString());
		date.setText(transaction.getStringDate());
		note.setText(transaction.getNote());
	}
	
	@Override
	protected void createInputs(JPanel pane)
	{
		super.createInputs(pane);
	}

	@Override
	protected void onOK()
	{
		transaction.setName(name.getText());
		transaction.setMoneySum(money.getText());
		transaction.setDate(date.getText());
		transaction.setNote(note.getText());
		((BudgetDataTable)parentFrame).refresh();
	}
	
	public Transaction getTransaction()
	{
		return transaction;
	}
}
