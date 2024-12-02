package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 29.11.2024
 * @version 29.11.2024
 */
public class EditTransactionMacro extends AddTransactionMacro
{
	public EditTransactionMacro(JFrame parentFrame)
	{
		super(parentFrame);
		this.setTitle("Edit Transaction Macro");
		pack();
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		super.createInputs(arg0);
		existingMacros.addActionListener(evt -> {
			TransactionMacro tm = MarvusDatabase.utils.getMacro(existingMacros.getSelectedIndex());
			name.setText(tm.name());
			SufuComponentTools.setSelectedItemGeneric(category, tm.category());
			value.setText(tm.value());
		});
		existingMacros.setSelectedIndex(0);
	}

	@Override
	protected void onOK()
	{
		if (name.getText().isBlank() || value.getText().isBlank())
			return;
		data = new TransactionMacro(name.getText(), SufuComponentTools.extractComboboxValue(category), value.getText());
		if (MarvusDatabase.utils.setMacro(SufuComponentTools.extractComboboxValue(existingMacros), data))
			closeDialog();
	}
}
