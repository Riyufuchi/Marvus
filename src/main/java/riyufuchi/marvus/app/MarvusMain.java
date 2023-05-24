package riyufuchi.marvus.app;

import java.io.IOException;
import java.util.LinkedList;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.windows.BudgetDataTable;
import riyufuchi.marvus.marvusLib.legacy.gui.DataTableForm;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 24.05.2023
 *
 * @author Riyufuchi
 */
public class MarvusMain
{
	private static SufuWindow app;
	
	public static void main(String[] args)
	{
		loadSettings();
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
	
	public static void loadSettings()
	{
		LinkedList<String> data = null;
		try
		{
			data = Persistance.loadTextFile(MarvusConfig.SETTINGS_FILE_PATH);
			MarvusConfig.appTheme = AppThemeUI.valueOf(data.getFirst()); // Throws exception when inputed incorrect data
		}
		catch (IllegalArgumentException | NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(app, e);
		}
	}
}
