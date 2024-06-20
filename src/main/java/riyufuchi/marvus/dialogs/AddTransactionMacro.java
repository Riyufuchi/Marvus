package riyufuchi.marvus.dialogs;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @version 1.0 - 12.10.2023
 * @since 1.97 - 12.10.2023
 */
public class AddTransactionMacro extends SufuDataDialog<TransactionMacro>
{
	private JTextField name, value;
	private JComboBox<String> category;
	
	public AddTransactionMacro(JFrame parentFrame)
	{
		super("New Transaction Macro", parentFrame, DialogType.OK);
		pack();
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		name = SufuFactory.newTextField("");
		value = SufuFactory.newTextField("0");
		category = SufuFactory.<String>newCombobox(MarvusDatabase.utils.getCategoryList());
		SufuGuiTools.addLabels(this, "Name:", "Category:", "Value:");
		SufuGuiTools.addComponents(this, 1, 0, name, category, value);
	}

	@Override
	protected void onOK()
	{
		if (name.getText().isBlank() || value.getText().isBlank())
			return;
		data = new TransactionMacro(name.getText(), SufuComponentTools.extractComboboxValue(category), value.getText());
		closeDialog();
	}
}
