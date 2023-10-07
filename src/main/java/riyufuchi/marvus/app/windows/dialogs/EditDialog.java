package riyufuchi.marvus.app.windows.dialogs;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * Dialog for editing Transaction
 * 
 * @author Riyufuchi
 * @version 07.10.2023
 * @since 11.05.2023
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
		categoryBox.setSelectedIndex(utils.getMacroIndex(utils.getCategories(), transaction.getCategory()));
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
		transaction.setCategory(SufuComponentTools.extractComboboxValue(categoryBox));
		transaction.setValue(money.getText());
		transaction.setDate(date.getText());
		transaction.setNote(note.getText());
		transaction.setCurrency(currency.getText());
		((MarvusDataWindow)parentFrame).refresh();
	}
	
	public Transaction getTransaction()
	{
		return transaction;
	}
}
