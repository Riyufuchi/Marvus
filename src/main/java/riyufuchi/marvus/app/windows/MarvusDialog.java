package riyufuchi.marvus.app.windows;

import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import riyufuchi.sufuLib.gui.DialogHelper;

/**
 * Created On: 24.04.2023<br>
 * Last Edit: 24.04.2023
 * <hr>
 * Abstract class for creating custom JDialog 
 * <hr>
 * @author Riyufuchi
 */
public abstract class MarvusDialog
{
	protected BudgetDataTable bdt;
	private String title;
	private JComponent[] inputs;
	private Consumer<JComponent[]> consumer;
	
	public MarvusDialog(String title, BudgetDataTable bdt)
	{
		init(title, bdt);
	}
	
	public MarvusDialog(String title, BudgetDataTable bdt, Consumer<JComponent[]> consumer)
	{
		init(title, bdt);
		this.consumer = consumer;
		if (consumer == null)
			this.consumer = con -> DialogHelper.errorDialog(bdt, "No action assigned", "Consumer error");
	}
	
	protected abstract JComponent[] createInputs();
	protected abstract Consumer<JComponent[]> consume();
	
	private void init(String title, BudgetDataTable bdt)
	{
		this.title = title;
		this.bdt = bdt;
		this.inputs = createInputs();
		if (inputs == null)
		{
			this.inputs = new JComponent[1];
			inputs[0] = new JLabel("Dialog text");
		}
		this.consumer = consume();
		if (consumer == null)
			this.consumer = con -> DialogHelper.errorDialog(bdt, "No action assigned", "Consumer error");
	}

	public void showDialog()
	{
		DialogHelper.customDialog(bdt, title, inputs, consumer);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
}
