package riyufuchi.marvus.dialogs.tools.names;

import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.records.SufuPair;
import riyufuchi.sufuLib.records.SufuSimpleRow;

/**
 * @author Riyufuchi
 * @since 02.12.2024
 * @version 10.01.2025
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
			SufuComponentTools.setSelectedItemGeneric(categoryInputCB, SufuComponentTools.extractComboboxValue(categoriesCB)));
		categoriesCB.setSelectedIndex(0);
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
		data = new SufuPair<>(SufuComponentTools.extractComboboxValue(categoriesCB), new SufuSimpleRow<>(0, (String)categoryInputCB.getEditor().getItem()));
		closeDialog();
	}
}
