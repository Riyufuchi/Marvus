package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 03.12.2024
 * @version 12.12.2024
 */
public class RemoveEntityName extends EditEntityName
{
	public RemoveEntityName(JFrame parentFrame, MarvusDatabase db)
	{
		super(parentFrame, db);
		setTitle("Remove Entity Name");
		pack();
	}

	@Override
	protected void createUI(JPanel panel)
	{
		super.createUI(panel);
		categoryInput.setEnabled(false);
	}
	
	@Override
	protected void createInputs(JPanel panel)
	{
	}
	
	@Override
	protected void onOK()
	{
		data = String.valueOf(SufuComponentTools.extractComboboxValue(categoriesCB).id());
		closeDialog();
	}
}
