package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.records.SufuRow;

/**
 * @author Riyufuchi
 * @since 29.11.2024
 * @version 16.01.2025
 */
public class EditTransactionMacro extends AddTransactionMacro
{
	protected SufuRow<String, TransactionMacro> selectedRow;
	
	public EditTransactionMacro(JFrame parentFrame, MarvusDatabaseController database)
	{
		super(parentFrame, database);
		this.setTitle("Edit Transaction Macro");
		pack();
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		super.createInputs(arg0);
		existingMacros.removeAllItems();
		existingMacros.addActionListener(evt -> {
			database.getMacrosTable().getByID(SufuComponentTools.extractComboboxValue(existingMacros)).ifPresent(row -> {
				selectedRow = new SufuRow<>(row.name(), row);
				name.setText(row.name());
				SufuComponentTools.setSelectedItemGeneric(category, row.category());
				value.setText(row.value());
			});
		});
		for (SufuRow<String, TransactionMacro> row : MarvusTableUtils.selectMacroOrdered(database.getMacrosTable()))
			existingMacros.addItem(row.entity().name());
		this.existingMacros.setSelectedIndex(0);
	}

	@Override
	protected void onOK()
	{
		if (name.getText().isBlank() || value.getText().isBlank() || selectedRow == null)
			return;
		data = new TransactionMacro(name.getText(), SufuComponentTools.extractComboboxValue(category), value.getText());
		closeDialog();
	}
}
