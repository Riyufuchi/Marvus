package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuGuiTools;
import riyufuchi.sufuLib.records.SufuPair;
import riyufuchi.sufuLib.records.SufuSimpleRow;

/**
 * This dialog return pair of SufuSimpleRows with String entity. Index one is used for indexing when editing or removing entity name from the database.
 * 
 * @author Riyufuchi
 * @since 02.12.2024
 * @version 10.01.2025
 */
public class AddEntityName extends SufuDataDialog<SufuPair<SufuSimpleRow<String>, SufuSimpleRow<String>>>
{
	protected JComboBox<SufuSimpleRow<String>> categoriesCB, categoryInputCB;
	protected MarvusDatabase database;
	
	public AddEntityName(JFrame parentFrame, MarvusDatabase database)
	{
		super("New Entity Name", parentFrame, DialogType.OK);
		this.database = database;
		createUI(getPane());
		pack();
	}
	
	protected void createUI(JPanel panel)
	{
		categoriesCB = SufuFactory.newCombobox(database.getEntitiesTableController().getRows());
		categoriesCB.setEnabled(false);
		categoryInputCB = SufuFactory.newCombobox(new SufuSimpleRow<>(0, ""));
		categoryInputCB.setEditable(true);
		SufuGuiTools.addLabels(this, "Old name:", "New name:");
		SufuGuiTools.addComponents(this, 1, 0, categoriesCB, categoryInputCB);
	}

	@Override
	protected void createInputs(JPanel panel)
	{
	}

	@Override
	protected void onOK()
	{
		if (((String)categoryInputCB.getEditor().getItem()).isBlank())
			return;
		data = new SufuPair<>(null, new SufuSimpleRow<String>(0, (String)categoryInputCB.getEditor().getItem()));
		closeDialog();
	}
}
