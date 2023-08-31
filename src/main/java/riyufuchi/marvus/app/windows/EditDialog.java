package riyufuchi.marvus.app.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.marvusLib.data.Transaction;

/**
 * Dialog for editing Transaction<br><br>
 * 
 * Created On: 11.05.2023<br>
 * Last Edit: 31.08.2023<br>
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
		nameBox.setSelectedItem(transaction.getName());
		name.setText(transaction.getName());
		category.setText(transaction.getCategory());
		money.setText(transaction.getValue().toString());
		date.setText(transaction.getStringDate());
		note.setText(transaction.getNote());
		currency.setText(transaction.getCurrency());
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
		transaction.setCategory(category.getText());
		transaction.setValue(money.getText());
		transaction.setDate(date.getText());
		transaction.setNote(note.getText());
		transaction.setCurrency(currency.getText());
		((BudgetDataTable)parentFrame).refresh();
	}
	
	public Transaction getTransaction()
	{
		return transaction;
	}
}
