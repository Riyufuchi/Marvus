package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JFrame;

import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 29.11.2024
 * @version 29.11.2024
 */
public class DeleteTransactionMacro extends EditTransactionMacro
{
	public DeleteTransactionMacro(JFrame parentFrame)
	{
		super(parentFrame);
		this.setTitle("Delete Transaction Macro");
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
