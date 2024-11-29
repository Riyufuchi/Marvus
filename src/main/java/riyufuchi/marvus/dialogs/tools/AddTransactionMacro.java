package riyufuchi.marvus.dialogs.tools;

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
 * @since 1.97 - 12.10.2023
 * @version 29.11.2024
 */
public class AddTransactionMacro extends SufuDataDialog<TransactionMacro>
{
	protected JTextField name, value;
	protected JComboBox<String> category, existingMacros;
	
	public AddTransactionMacro(JFrame parentFrame)
	{
		super("New Transaction Macro", parentFrame, DialogType.OK);
		pack();
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		existingMacros = SufuFactory.newCombobox(MarvusDatabase.utils.getNames());
		existingMacros.setEnabled(false);
		name = SufuFactory.newTextField("");
		value = SufuFactory.newTextField("0");
		category = SufuFactory.<String>newCombobox(MarvusDatabase.utils.getCategoryEnum());
		SufuGuiTools.addLabels(this, "Name:", null, "Category:", "Value:");
		SufuGuiTools.addComponents(this, 1, 0, existingMacros, name, category, value);
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
