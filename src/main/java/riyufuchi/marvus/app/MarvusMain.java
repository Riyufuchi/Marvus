package riyufuchi.marvus.app;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.windows.BudgetDataTable;
import riyufuchi.marvus.marvusLib.legacy.gui.DataTableForm;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.gui.SufuWindow;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 17.04.2023
 *
 * @author Riyufuchi
 */
public class MarvusMain
{
	private static SufuWindow app;
	
	public static void main(String[] args)
	{
		CustomizeUI.setUI(MarvusConfig.appTheme);
		for(String arg : args)
		{
			if(arg.equals("--legacy"))
				app = new DataTableForm(800, 600);
		}
		if(app == null)
			app = new BudgetDataTable();
		app.toFront();
	}
}
