package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 02.12.2024
 * @version 02.12.2024
 */
public class AddEntityName extends SufuDataDialog<String>
{
	protected JComboBox<String> categoriesCB;
	protected JTextField categoryInput;
	
	public AddEntityName(JFrame parentFrame)
	{
		super("New Entity Name", parentFrame, DialogType.OK);
		pack();
	}

	@Override
	protected void createInputs(JPanel panel)
	{
		categoriesCB = SufuFactory.newCombobox(MarvusDatabase.utils.getEntityNamesEnum());
		categoriesCB.setEnabled(false);
		categoryInput = SufuFactory.newTextField("");
		SufuGuiTools.addLabels(this, "Name:");
		SufuGuiTools.addComponents(this, 1, 0, categoriesCB, categoryInput);
	}

	@Override
	protected void onOK()
	{
		if (categoryInput.getText().isBlank())
			return;
		data = categoryInput.getText();
		closeDialog();
	}
}
