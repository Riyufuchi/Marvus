package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.records.SufuPair;
import riyufuchi.sufuLib.records.SufuSimpleRow;

/**
 * @author Riyufuchi
 * @since 03.12.2024
 * @version 10.01.2025
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
		categoryInputCB.removeAllItems();
		for (SufuSimpleRow<String> row : database.getEntitiesTableController().getRows())
			categoryInputCB.addItem(row);
		categoryInputCB.setEditable(false);
	}
	
	@Override
	protected void createInputs(JPanel panel)
	{
	}
	
	@Override
	protected void onOK()
	{
		if (SufuComponentTools.extractComboboxValue(categoriesCB).entity().equals(SufuComponentTools.extractComboboxValue(categoryInputCB).entity()))
			return;
		data = new SufuPair<>(SufuComponentTools.extractComboboxValue(categoriesCB), SufuComponentTools.extractComboboxValue(categoryInputCB));
		closeDialog();
	}
}
