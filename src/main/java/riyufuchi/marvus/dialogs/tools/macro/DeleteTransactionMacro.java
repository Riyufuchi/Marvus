package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JFrame;

import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;

/**
 * @author Riyufuchi
 * @since 29.11.2024
 * @version 11.01.2025
 */
public class DeleteTransactionMacro extends EditTransactionMacro
{
	public DeleteTransactionMacro(JFrame parentFrame, MarvusDatabaseController database)
	{
		super(parentFrame, database);
		this.setTitle("Delete Transaction Macro");
		value.setEnabled(false);
		category.setEnabled(false);
	}
	
	@Override
	protected void onOK()
	{
		data = selectedRow.entity();
		closeDialog();
	}
}
