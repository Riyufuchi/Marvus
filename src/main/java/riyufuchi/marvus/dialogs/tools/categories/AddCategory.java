package riyufuchi.marvus.dialogs.tools.categories;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuGuiTools;
import riyufuchi.sufuLib.records.SufuSimpleRow;

/**
 * @author Riyufuchi
 * @since 1.97 - 12.10.2023
 * @version 10.01.2025
 */
public class AddCategory extends SufuDataDialog<SufuSimpleRow<String>>
{
	protected JComboBox<SufuSimpleRow<String>> categoriesCB;
	protected JTextField categoryInput;
	protected MarvusDatabase database;
	
	public AddCategory(JFrame parentFrame, MarvusDatabase database)
	{
		super("New Category", parentFrame, DialogType.OK);
		this.database = database;
		createUI(getPane());
		pack();
	}

	protected void createUI(JPanel panel)
	{
		categoriesCB = SufuFactory.newCombobox(database.getCategoriesTableController().getRows());
		categoriesCB.setEnabled(false);
		categoryInput = SufuFactory.newTextField("");
		SufuGuiTools.addLabels(this, "Category:");
		SufuGuiTools.addComponents(this, 1, 0, categoriesCB, categoryInput);
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
		data = new SufuSimpleRow<String>(-1, categoryInput.getText());
		closeDialog();
	}
}
