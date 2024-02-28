package riyufuchi.marvus.app.windows.dialogs;

import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.Money;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.database.MaruvsDatabaseUtils;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.SufuDatePicker;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

/**
 * Dialog for adding new transaction. Also base class for other dialogs regarding transactions.<br><br>
 *
 * @author Riyufuchi
 * @since 16.05.2023
 * @version 28.02.2024
 */
public class AddDialog extends SufuDialog
{
	protected JTextField name, money, currency;
	protected JButton date;
	protected JComboBox<String> nameBox, categoryBox;
	protected JTextArea note;
	protected MaruvsDatabaseUtils utils;
	protected LocalDateTime localDate;
	
	public AddDialog(JFrame parentFrame)
	{
		super("New transaction", parentFrame, DialogType.OK, true, true);
	}
	
	@Override
	protected void createInputs(JPanel pane)
	{
		utils = MarvusDatabase.utils;
		nameBox = SufuFactory.<String>newCombobox(utils.getNames());
		categoryBox = SufuFactory.<String>newCombobox(utils.getCategoryList());
		name = SufuFactory.newTextField("");
		money = SufuFactory.newTextField("");
		date = SufuFactory.newButton(SufuDateUtils.nowDateString(), evt -> {
			localDate = new SufuDatePicker(parentFrame).showAndGet();
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
			int i = 0;
			for (i = 0; i < categoryBox.getItemCount(); i++)
			{
				if (categoryBox.getItemAt(i).equals(utils.getCategories()[nameBox.getSelectedIndex()]))
				{
					categoryBox.setSelectedIndex(i);
					break;
				}
			}
			if (i == categoryBox.getItemCount())
				categoryBox.setSelectedIndex(0);
			money.setText(utils.getValues()[nameBox.getSelectedIndex()]);
			if (money.getText().equals("0"))
				money.setText("");
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
		((MarvusDataWindow)parentFrame).getDatabase().add(new Transaction(name.getText(), SufuComponentTools.<String>extractComboboxValue(categoryBox), money.getText(), date.getText(), note.getText()));
		((MarvusDataWindow)parentFrame).refresh();
	}
}
