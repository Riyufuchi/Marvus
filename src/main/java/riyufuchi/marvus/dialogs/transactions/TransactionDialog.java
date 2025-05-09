package riyufuchi.marvus.dialogs.transactions;

import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import riyufuchi.marvusLib.data.Money;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.marvusLib.enums.MarvusAction;
import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;
import riyufuchi.sufuLib.general.SufuInputChecker;
import riyufuchi.sufuLib.gui.SufuDataDialogGeneric;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuGuiTools;
import riyufuchi.sufuLib.time.SufuDateUtils;

/**
 * @author riyufuchi
 * @since 17.01.2025
 * @version 17.01.2025
 */
public class TransactionDialog extends SufuDataDialogGeneric<JFrame, Transaction>
{
	private MarvusDatabaseController database;
	private MarvusAction dialogAction;
	private Transaction transaction;
	// UI
	private JTextField money, currency;
	private JButton date;
	private JComboBox<String> nameBox, categoryBox;
	private JTextArea note;
	private LocalDateTime localDate;
	
	public TransactionDialog(JFrame parentFrame, MarvusDatabaseController database, MarvusAction dialogAction)
	{
		super("Marvus Transaction", parentFrame, DialogType.OK, false, false, false, new Object[]{ dialogAction.toString() });
		this.dialogAction = dialogAction;
		this.database = database;
		if (dialogAction == null || database == null) 
			this.dialogAction = MarvusAction.NONE;
		if (localDate == null)
			localDate = LocalDateTime.now();
		switch (this.dialogAction)
		{
			case ADD, NONE -> {
				createInputs(getPane());
				pack();
			}
			default -> {}
		}
	}
	
	public TransactionDialog(JFrame parentFrame, MarvusDatabaseController database, MarvusAction dialogAction, Transaction transaction)
	{
		this(parentFrame, database, dialogAction);
		this.transaction = transaction;
		createInputs(getPane());
		pack();
	}
	
	private void createAddUI(JPanel pane)
	{
		getGBC(0, 0).weightx = 1.0;
		nameBox = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.getEntitiesTable().getData()));
		categoryBox = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.getCategoriesTable().getData()));
		money = SufuFactory.newTextField("");
		date = SufuFactory.newButton(SufuDateUtils.nowDateString(), evt -> {
			localDate = new SufuDatePicker(parentFrame, localDate).showAndGet();
			date.setText((localDate.getDayOfMonth() + "." + localDate.getMonthValue() + "." + localDate.getYear()));
		});
		currency = SufuFactory.newTextField(Money.getDefaultCurrency());
		note = SufuFactory.newTextArea("");
		nameBox.addActionListener(evt -> {
			database.getMacrosTable().getByID(SufuComponentTools.extractComboboxValue(nameBox)).ifPresent(row -> {
				SufuComponentTools.setSelectedItemGeneric(categoryBox, row.category());
				if (row.value().equals("0"))
					money.setText("");
				else
					money.setText(row.value());	
			});
		});
		SufuComponentTools.setSelectedItem(nameBox, "Custom");
		SufuGuiTools.addLabels(this, "Name:", "Category", "Amount:", "Currency: ", "Date:", "Note:");
		SufuGuiTools.addComponents(this, 1, 0, nameBox, categoryBox, money, currency, date, note);
	}
	
	private void createEditUI(JPanel pane)
	{
		createAddUI(pane);
		SufuComponentTools.setSelectedItemGeneric(nameBox, transaction.getName());
		SufuComponentTools.setSelectedItemGeneric(categoryBox, transaction.getCategory());
		money.setText(transaction.getValue().toString());
		date.setText(transaction.getStringDate());
		note.setText(transaction.getNote());
		currency.setText(transaction.getCurrency());
		localDate = transaction.getDate();
	}
	
	private void createDeleteUI(JPanel pane)
	{
		createEditUI(pane);
		SufuComponentTools.disableAll(nameBox, categoryBox, money, date, note, currency);
	}
	
	// Overrides

	@Override
	protected void createInputs(JPanel pane)
	{
		switch (dialogAction)
		{
			case ADD -> createAddUI(pane);
			case EDIT -> createEditUI(pane);
			case DELETE -> createDeleteUI(pane);
			default -> SufuDialogHelper.errorDialog(parentFrame, "Incorrect dialog action: " + dialogAction.toString(), getTitle() + "error");
		}
	}

	@Override
	protected void onOK()
	{
		switch (dialogAction)
		{
			case ADD -> onOkAdd();
			case EDIT -> onOkEdit();
			case DELETE -> onOkDelete();
			default -> closeDialog();
		}
	}

	// On methods

	private void onOkAdd()
	{
		try
		{
			data = new Transaction(SufuComponentTools.extractComboboxValue(nameBox), SufuComponentTools.<String>extractComboboxValue(categoryBox), 
					SufuInputChecker.checkDoubleFormat(money.getText()), currency.getText(), date.getText(), note.getText());
		}
		catch (Exception e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		closeDialog();
	}
	
	private void onOkEdit()
	{
		transaction.setName(SufuComponentTools.extractComboboxValue(nameBox));
		transaction.setCategory(SufuComponentTools.extractComboboxValue(categoryBox));
		try
		{
			transaction.setValue(SufuInputChecker.checkDoubleFormat(money.getText()));
		}
		catch (Exception e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		transaction.setCurrency(currency.getText());
		transaction.setDate(date.getText());
		transaction.setNote(note.getText());
		data = transaction;
		closeDialog();
	}

	private void onOkDelete()
	{
		data = transaction;
		closeDialog();
	}
}
