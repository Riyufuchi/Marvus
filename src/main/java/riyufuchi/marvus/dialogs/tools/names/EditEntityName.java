package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 02.12.2024
 * @version 02.12.2024
 */
public class EditEntityName extends AddEntityName
{
	public EditEntityName(JFrame parentFrame)
	{
		super(parentFrame);
		setTitle("Edit Entity Name");
		pack();
	}

	@Override
	protected void createInputs(JPanel panel)
	{
		super.createInputs(panel);
		categoriesCB.setEnabled(true);
		categoriesCB.addActionListener(evt -> 
		{
			categoryInput.setText(SufuComponentTools.extractComboboxValue(categoriesCB));
		});
		categoriesCB.setSelectedIndex(0);
	}
	
	@Override
	protected void onOK()
	{
		if (categoryInput.getText().isBlank())
			return;
		data = categoryInput.getText() + " " + SufuComponentTools.extractComboboxValue(categoriesCB);
		closeDialog();
	}
}
