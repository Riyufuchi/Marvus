package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.LinkedList;

import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.database.MarvusTableUtils;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.records.SufuRowDB;

/**
 * @author Riyufuchi
 * @since 29.11.2024
 * @version 12.12.2024
 */
public class EditTransactionMacro extends AddTransactionMacro
{
	protected LinkedList<SufuRowDB<String, TransactionMacro>> rows;
	protected SufuRowDB<String, TransactionMacro> selectedRow;
	
	public EditTransactionMacro(JFrame parentFrame, MarvusDatabase database)
	{
		super(parentFrame, database);
		this.setTitle("Edit Transaction Macro");
		this.rows = MarvusTableUtils.selectMacroOrdered(database.macroTable);
		for (SufuRowDB<String, TransactionMacro> row : rows)
			existingMacros.addItem(row.entity().name());
		this.existingMacros.setSelectedIndex(0);
		pack();
	}
	
	protected void createUI(JPanel arg0)
	{
		super.createUI(arg0);
		existingMacros.removeAllItems();
		existingMacros.addActionListener(evt -> {
			database.macroTable.getByID(SufuComponentTools.extractComboboxValue(existingMacros)).ifPresent(row -> {
				selectedRow = new SufuRowDB<>(row.name(), row);
				name.setText(row.name());
				SufuComponentTools.setSelectedItemGeneric(category, row.category());
				value.setText(row.value());
			});
		});
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		
	}

	@Override
	protected void onOK()
	{
		if (name.getText().isBlank() || value.getText().isBlank() || selectedRow == null)
			return;
		data = new TransactionMacro(name.getText(), SufuComponentTools.extractComboboxValue(category), value.getText());
		database.macroTable.set(data.name(), data);
		closeDialog();
	}
}
