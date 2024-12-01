package riyufuchi.marvus.dialogs.tools;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 1.97 - 12.10.2023
 * @version 30.11.2024
 */
public class AddCategory extends SufuDataDialog<String[]>
{
	protected JComboBox<String> categoriesCB;
	protected JTextField categoryInput;
	
	public AddCategory(JFrame parentFrame)
	{
		super("New Category", parentFrame, DialogType.OK);
		pack();
	}

	@Override
	protected void createInputs(JPanel panel)
	{
		categoriesCB = SufuFactory.newCombobox(MarvusDatabase.utils.getCategoryEnum());
		categoriesCB.setEnabled(false);
		categoryInput = SufuFactory.newTextField("");
		SufuGuiTools.addLabels(this, "Category:");
		SufuGuiTools.addComponents(this, 1, 0, categoriesCB, categoryInput);
	}

	@Override
	protected void onOK()
	{
		if (categoryInput.getText().isBlank())
			return;
		if (categoryInput.getText().contains(";"))
			data = categoryInput.getText().split(";");
		else
			data = new String[] { categoryInput.getText() };
		closeDialog();
	}
}
