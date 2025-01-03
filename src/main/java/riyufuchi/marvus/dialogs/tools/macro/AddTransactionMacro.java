package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 1.97 - 12.10.2023
 * @version 12.12.2024
 */
public class AddTransactionMacro extends SufuDataDialog<TransactionMacro>
{
	protected JTextField name, value;
	protected JComboBox<String> category, existingMacros;
	protected MarvusDatabase database;
	
	public AddTransactionMacro(JFrame parentFrame, MarvusDatabase database)
	{
		super("New Transaction Macro", parentFrame, DialogType.OK);
		this.database = database;
		createUI(getPane());
		pack();
	}

	protected void createUI(JPanel arg0)
	{
		existingMacros = SufuFactory.newCombobox(database.entities.getData());
		name = SufuFactory.newTextField("");
		name.setEnabled(false);
		value = SufuFactory.newTextField("0");
		category = SufuFactory.newCombobox(database.categories.getData());
		SufuGuiTools.addLabels(this, "Name:", null, "Category:", "Value:");
		SufuGuiTools.addComponents(this, 1, 0, existingMacros, name, category, value);
	}
	
	@Override
	protected void createInputs(JPanel arg0)
	{
		
	}

	@Override
	protected void onOK()
	{
		if (value.getText().isBlank())
			return;
		data = new TransactionMacro(SufuComponentTools.extractComboboxValue(existingMacros), SufuComponentTools.extractComboboxValue(category), value.getText());
		closeDialog();
	}
}
