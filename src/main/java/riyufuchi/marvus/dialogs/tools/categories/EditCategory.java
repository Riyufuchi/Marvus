package riyufuchi.marvus.dialogs.tools.categories;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.records.SufuSimpleRow;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;

/**
 * @author Riyufuchi
 * @since 30.11.2024
 * @version 01.12.2024
 */
public class EditCategory extends AddCategory
{
	public EditCategory(JFrame parentFrame, MarvusDatabase database)
	{
		super(parentFrame, database);
		setTitle("Edit category");
		pack();
	}

	@Override
	protected void createUI(JPanel panel)
	{
		super.createUI(panel);
		categoriesCB.setEnabled(true);
		categoriesCB.addActionListener(evt -> 
		{
			categoryInput.setText(SufuComponentTools.extractComboboxValue(categoriesCB).entity());
		});
		categoriesCB.setSelectedIndex(0);
	}
	
	@Override
	protected void onOK()
	{
		if (categoryInput.getText().isBlank())
			return;
		data = new SufuSimpleRow<>(SufuComponentTools.extractComboboxValue(categoriesCB).id(), categoryInput.getText());
		closeDialog();
	}
}
