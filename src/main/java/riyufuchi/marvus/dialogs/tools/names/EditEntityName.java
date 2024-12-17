package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 02.12.2024
 * @version 12.12.2024
 */
public class EditEntityName extends AddEntityName
{
	public EditEntityName(JFrame parentFrame, MarvusDatabase db)
	{
		super(parentFrame, db);
		setTitle("Edit Entity Name");
		pack();
	}
	
	@Override
	protected void createUI(JPanel panel)
	{
		super.createUI(panel);
		categoriesCB.setEnabled(true);
		categoriesCB.addActionListener(evt -> 
		{
			categoryInput.setText(SufuComponentTools.extractComboboxValue(categoriesCB).toString());
		});
		categoriesCB.setSelectedIndex(0);
	}

	@Override
	protected void createInputs(JPanel panel)
	{
	}
	
	@Override
	protected void onOK()
	{
		if (categoryInput.getText().isBlank())
			return;
		data = categoryInput.getText() + ";" + (SufuComponentTools.extractComboboxValue(categoriesCB).id());
		closeDialog();
	}
}
