package riyufuchi.marvus.dialogs;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * Dialog for editing Transaction
 * 
 * @author Riyufuchi
 * @since 11.05.2023
 * @version 18.06.2024
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
	}
	
	public Transaction getTransaction()
	{
		return transaction;
	}
}
