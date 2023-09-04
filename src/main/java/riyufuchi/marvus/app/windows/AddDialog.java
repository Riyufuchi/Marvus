package riyufuchi.marvus.app.windows;


import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import riyufuchi.marvus.app.utils.MarvusCategory;
import riyufuchi.marvus.marvusLib.data.Money;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

/**
 * Dialog for adding new transaction. Also base class for other dialogs regarding transactions.<br><br>
 * 
 * Created On: 16.05.2023<br>
 * Last Edit: 01.09.2023
 * 
 * @author Riyufuchi
 */
public class AddDialog extends SufuDialog
{
	protected JTextField name, category,  money, date, currency;
	protected JComboBox<String> nameBox, categoryBox;
	protected JTextArea note;
	
	public AddDialog(JFrame parentFrame)
	{
		super("New transaction", parentFrame, DialogType.OK, true, true);
	}
	
	@Override
	protected void createInputs(JPanel pane)
	{
		nameBox = SufuFactory.<String>newCombobox(MarvusCategory.names);
		categoryBox = SufuFactory.<String>newCombobox(MarvusCategory.categoryList);
		name = SufuFactory.newTextField("");
		category = SufuFactory.newTextField("Other");
		money = SufuFactory.newTextField("");
		date = SufuFactory.newTextField(DateUtils.nowDateString());
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
				if (categoryBox.getItemAt(i).equals(MarvusCategory.categories[nameBox.getSelectedIndex()]))
				{
					categoryBox.setSelectedIndex(i);
					break;
				}
			}
			if (i == categoryBox.getItemCount())
				categoryBox.setSelectedIndex(0);
			money.setText(MarvusCategory.values[nameBox.getSelectedIndex()]);
			if (money.getText().equals("0"))
				money.setText("");
		});
		categoryBox.addActionListener(evt -> {
			if (categoryBox.getItemAt(categoryBox.getSelectedIndex()).equals("Other"))
				category.setEnabled(true);
			else
				category.setEnabled(false);
			category.setText(categoryBox.getItemAt(categoryBox.getSelectedIndex()));
		});
		// Set labels
		pane.add(new JLabel("Name:"), getGBC(0, 0));
		pane.add(new JLabel("Category:"), getGBC(0, 2));
		int y = 4;
		for (String text : new String[]{ "Amount:", "Currency: ", "Date:", "Note:" })
			pane.add(new JLabel(text), getGBC(0, y++));
		y = 0;
		// Set components
		for (JComponent comp : new JComponent[]{ nameBox, name, categoryBox, category, money, currency, date, note})
			pane.add(comp, getGBC(1, y++));
	}
	@Override
	protected void onOK()
	{
		((BudgetDataTable)parentFrame).getDataBox().add(new Transaction(name.getText(), category.getText(), money.getText(), date.getText(), note.getText()));
		((BudgetDataTable)parentFrame).refresh();
	}
}
