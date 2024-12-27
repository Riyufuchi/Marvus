package riyufuchi.marvus.dialogs.transactions;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.enums.UserAction;
import riyufuchi.marvusLib.records.LastChange;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * Dialog for editing Transaction
 * 
 * @author Riyufuchi
 * @since 11.05.2023
 * @version 12.12.2024
 */
public class EditTransactionDialog extends AddTransactionDialog
{
	private Transaction transaction;
	
	public EditTransactionDialog(JFrame parentFrame, Transaction transaction, MarvusDatabase database)
	{
		super(parentFrame, database);
		this.transaction = transaction;
		prefillComponents();
		setTitle("Edit transaction " + transaction.getID());
		pack();
	}

	private void prefillComponents()
	{
		SufuComponentTools.setSelectedItemGeneric(nameBox, transaction.getName());
		name.setText(transaction.getName());
		SufuComponentTools.setSelectedItemGeneric(categoryBox, transaction.getCategory());
		money.setText(transaction.getValue().toString());
		date.setText(transaction.getStringDate());
		note.setText(transaction.getNote());
		currency.setText(transaction.getCurrency());
		localDate = transaction.getDate();
	}
	
	@Override
	protected void createUI(JPanel pane)
	{
		super.createUI(pane);
		nameBox.removeActionListener(nameBox.getActionListeners()[0]);
		nameBox.addActionListener(evt -> {
			if (nameBox.getItemAt(nameBox.getSelectedIndex()).equals("Custom"))
			{
				name.setEnabled(true);
				name.setText("");
			}
			else
			{
				name.setEnabled(false);
				name.setText(nameBox.getItemAt(nameBox.getSelectedIndex()));
			}
		});
	}

	@Override
	protected void createInputs(JPanel pane)
	{
	}

	@Override
	protected void onOK()
	{
		transaction.setName(name.getText());
		transaction.setCategory(SufuComponentTools.extractComboboxValue(categoryBox));
		transaction.setValue(money.getText());
		transaction.setDate(date.getText());
		transaction.setNote(note.getText());
		transaction.setCurrency(currency.getText());
		((MarvusDataWindow)parentFrame).getController().refresh();
		((MarvusDataWindow)parentFrame).getController().setLastAction(new LastChange(UserAction.EDIT, transaction));
	}
	
	public Transaction getTransaction()
	{
		return transaction;
	}
}
