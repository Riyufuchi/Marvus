package riyufuchi.marvus.app.windows;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;

/**
 * Created On: 24.04.2023<br>
 * Last Edit: 24.04.2023
 * <hr>
 * Abstract class for creating custom JDialog 
 * <hr>
 * @author Riyufuchi
 */
public abstract class MarvusGridDialog
{
	protected BudgetDataTable bdt;
	private String title;
	private JPanel pane;
	private GridBagConstraints gbc;
	private Consumer<Integer> consumer;
	
	public MarvusGridDialog(String title, BudgetDataTable bdt)
	{
		init(title, bdt);
	}
	
	public MarvusGridDialog(String title, BudgetDataTable bdt, Consumer<Integer> consumer)
	{
		init(title, bdt);
		this.consumer = consumer;
		if (consumer == null)
			this.consumer = con -> DialogHelper.errorDialog(bdt, "No action assigned", "Consumer error");
	}
	
	protected abstract void createInputs(JPanel pane);
	protected abstract Consumer<Integer> consume();
	
	private void init(String title, BudgetDataTable bdt)
	{
		this.title = title;
		this.bdt = bdt;
		this.gbc = new GridBagConstraints();
		this.gbc.fill = GridBagConstraints.HORIZONTAL;
		this.pane = FactoryComponent.newPane(new GridBagLayout());
		createInputs(pane);
		this.consumer = consume();
		if (consumer == null)
			this.consumer = con -> DialogHelper.errorDialog(bdt, "No action assigned", "Consumer error");
	}

	public void showDialog()
	{
		pane.revalidate();
		DialogHelper.customDialog(bdt, title, pane, consumer);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Set position of the component
	 *
	 * @param x - column position
	 * @param y - row position
	 * @return modified instance of GridBagConstraints
	 */
	public final GridBagConstraints getGBC(int x, int y)
	{
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}
}
