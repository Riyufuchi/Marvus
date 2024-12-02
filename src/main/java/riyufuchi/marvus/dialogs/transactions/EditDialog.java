package riyufuchi.marvus.dialogs.transactions;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.enums.UserAction;
import riyufuchi.marvusLib.records.LastChange;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * Dialog for editing Transaction
 * 
 * @author Riyufuchi
 * @since 11.05.2023
 * @version 02.12.2024
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
		SufuComponentTools.setSelectedItem(categoryBox, transaction.getCategory());
		money.setText(transaction.getValue().toString());
		date.setText(transaction.getStringDate());
		note.setText(transaction.getNote());
		currency.setText(transaction.getCurrency());
		localDate = transaction.getDate();
	}
	
	@Override
	protected void createInputs(JPanel pane)
	{
		super.createInputs(pane);
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
			SufuComponentTools.setSelectedItemGeneric(categoryBox, utils.getTransactionMacros().get(nameBox.getSelectedIndex()).category());
		});
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
