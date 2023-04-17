package riyufuchi.marvus.app;

import riyufuchi.marvus.app.gui.windows.BudgetDataTable;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.sufuLib.gui.utils.CustomizeUI;


/**
 * Created On: 20.04.2022<br>
 * Last Edit: 17.04.2023
 *
 * @author Riyufuchi
 */

public class MarvusMain
{
	private static BudgetDataTable budgetTable;
	
	public static void main(String[] args)
	{
		CustomizeUI.setUI(MarvusConfig.appTheme);
		budgetTable = new BudgetDataTable();
		//new DataTableForm(800, 600);
	}
}
