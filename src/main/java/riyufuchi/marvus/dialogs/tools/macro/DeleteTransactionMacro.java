package riyufuchi.marvus.dialogs.tools.macro;

import javax.swing.JFrame;

import riyufuchi.marvus.database.MarvusDatabase;

/**
 * @author Riyufuchi
 * @since 29.11.2024
 * @version 12.12.2024
 */
public class DeleteTransactionMacro extends EditTransactionMacro
{
	public DeleteTransactionMacro(JFrame parentFrame, MarvusDatabase database)
	{
		super(parentFrame, database);
		this.setTitle("Delete Transaction Macro");
	}
	
	@Override
	protected void onOK()
	{
		if (name.getText().isBlank() || selectedRow == null)
			return;
		data = selectedRow.entity();
		closeDialog();
	}
}
