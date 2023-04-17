package riyufuchi.marvus.app.gui.windows;

import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.marvusLib.legacy.gui.DataTableForm;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.ErrorWindow;
import riyufuchi.sufuLib.gui.Window;
import riyufuchi.sufuLib.gui.utils.JMenuCreator;

/**
 * Created On: 17.04.2023
 * Last Edit: 17.04.2023
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public class BudgetDataTable extends Window
{
	public BudgetDataTable()
	{
		super("Marvus - Budget table", 800, 600, false, true, true);
		// TODO Auto-generated constructor stub
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(AppTexts.BUDGET_TABLE_MENU, AppTexts.BUDGET_TABLE_MENU_ITEMS, 3);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				case "About" -> jmc.setItemAction(i, event -> about());
				case "Exit" -> jmc.setItemAction(i, event -> onClose());
				//case "Export"-> jmc.setItemAction(i,event -> exportData());
				//case "Import" -> jmc.setItemAction(i, event -> importData());
				//case "Refresh" -> jmc.setItemAction(i,event -> refresh());
				case "Data table - money sum" -> jmc.setItemAction(i,event -> {
					new DataTableForm(800, 600);
					this.dispose();
				});
				//case "Date" -> jmc.setItemAction(i,event -> sort());
				case "Preferences" -> jmc.setItemAction(i,event -> new Settings());
				//case "Backup" -> jmc.setItemAction(i,event -> backupData());
				default -> jmc.setItemAction(i, event -> DialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void about()
	{
		new ErrorWindow("About", "This is budget manager.\nVersion: " + AppTexts.VERSION + "\nCreated by Riyufuchi.\nMy code is under respective licention.");
	}
	
	@Override
	protected void setComponents(JPanel content)
	{
		setupJMenu();
	}
	
	@Override
	protected void onClose()
	{
		if(DialogHelper.yesNoDialog(this, "Exit application?", "Exit confirmation") == 0)
			super.dispose();
	}
}
