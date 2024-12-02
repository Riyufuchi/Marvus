package riyufuchi.marvus.dialogs.tools.categories;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 30.11.2024
 * @version 01.12.2024
 */
public class EditCategory extends AddCategory
{
	public EditCategory(JFrame parentFrame)
	{
		super(parentFrame);
		setTitle("Edit category");
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
		data = new String[] { SufuComponentTools.extractComboboxValue(categoriesCB), categoryInput.getText() };
		closeDialog();
	}
}
