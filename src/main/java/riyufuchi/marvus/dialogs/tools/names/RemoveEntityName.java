package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Riyufuchi
 * @since 03.12.2024
 * @version 03.12.2024
 */
public class RemoveEntityName extends EditEntityName
{
	public RemoveEntityName(JFrame parentFrame)
	{
		super(parentFrame);
		setTitle("Remove Entity Name");
		pack();
	}

	@Override
	protected void createInputs(JPanel panel)
	{
		super.createInputs(panel);
		categoryInput.setEnabled(false);
	}
	
	@Override
	protected void onOK()
	{
		data = String.valueOf(categoriesCB.getSelectedIndex());
		closeDialog();
	}
}
