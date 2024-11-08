package riyufuchi.marvus.dialogs.tools;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import riyufuchi.sufuLib.gui.SufuDataDialog;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @version 1.0 - 12.10.2023
 * @since 1.97 - 12.10.2023
 */
public class AddCategory extends SufuDataDialog<String[]>
{
	private JTextArea categoryInput;
	
	public AddCategory(JFrame parentFrame)
	{
		super("New Category", parentFrame, DialogType.OK, true, false);
		this.setBounds(getX(), getY(), 300, 200);
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		categoryInput = SufuFactory.newTextArea("category1;category2");
		arg0.add(new JLabel("New Category:"), getGBC(0, 0));
		//SufuGuiTools.addLabels(this, "New Category:");
		SufuGuiTools.addComponents(this, 1, 0, categoryInput);
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
		this.dispose();
	}
}
