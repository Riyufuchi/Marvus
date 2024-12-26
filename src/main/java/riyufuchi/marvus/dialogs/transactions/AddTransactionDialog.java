package riyufuchi.marvus.dialogs.transactions;

import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.data.Money;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.marvusLib.enums.UserAction;
import riyufuchi.marvusLib.records.LastChange;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * Dialog for adding new transaction. Also base class for other dialogs regarding transactions.
 *
 * @author Riyufuchi
 * @since 16.05.2023
 * @version 24.12.2024
 */
public class AddTransactionDialog extends SufuDialog
{
	protected JTextField name, money, currency;
	protected JButton date;
	protected JComboBox<String> nameBox, categoryBox;
	protected JTextArea note;
	protected LocalDateTime localDate;
	protected MarvusDatabase database;
	
	public AddTransactionDialog(JFrame parentFrame, MarvusDatabase database)
	{
		super("New transaction", parentFrame, DialogType.OK, true, true);
		if (localDate == null)
			localDate = LocalDateTime.now();
		this.database = database;
		createUI(getPane());
		pack();
	}
	
	protected void createUI(JPanel pane)
	{
		getGBC(0, 0).weightx = 1.0;
		nameBox = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.entities.getData()));
		categoryBox = SufuFactory.newCombobox(MarvusTableUtils.selectOrdered(database.categories.getData()));
		name = SufuFactory.newTextField("");
		money = SufuFactory.newTextField("");
		date = SufuFactory.newButton(SufuDateUtils.nowDateString(), evt -> {
			localDate = new SufuDatePicker(parentFrame, localDate).showAndGet();
			date.setText((localDate.getDayOfMonth() + "." + localDate.getMonthValue() + "." + localDate.getYear()));
		});
		currency = SufuFactory.newTextField(Money.getDefaultCurrency());
		note = SufuFactory.newTextArea("");
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
			database.macroTable.getByID(name.getText()).ifPresent(row -> {
				SufuComponentTools.setSelectedItemGeneric(categoryBox, row.category());
				if (row.value().equals("0"))
					money.setText("");
				else
					money.setText(row.value());	
			});
		});
		SufuComponentTools.setSelectedItem(nameBox, "Custom");
		// Set labels
		pane.add(new JLabel("Name:"), getGBC(0, 0));
		pane.add(new JLabel("Category:"), getGBC(0, 2));
		SufuGuiTools.addLabels(this, 0, 3, new String[]{ "Amount:", "Currency: ", "Date:", "Note:" });
		SufuGuiTools.addComponents(this, 1, 0, nameBox, name, categoryBox, money, currency, date, note);
	}
	
	
	@Override
	protected void onOK()
	{
		if (nameBox.getItemAt(nameBox.getSelectedIndex()).equals("Custom"))
			database.entities.add(name.getText());
		Transaction t = new Transaction(name.getText(), SufuComponentTools.<String>extractComboboxValue(categoryBox), money.getText(), date.getText(), note.getText());
		((MarvusDataWindow)parentFrame).getController().getDatabase().add(t);
		((MarvusDataWindow)parentFrame).getController().refresh();
		((MarvusDataWindow)parentFrame).getController().setLastAction(new LastChange(UserAction.ADD, t));
	}

	@Override
	protected void createInputs(JPanel pane)
	{
	}
}
