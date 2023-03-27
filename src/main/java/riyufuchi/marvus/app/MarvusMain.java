package riyufuchi.marvus.app;

import riyufuchi.marvus.gui.windows.DataTableForm;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.utils.CustomizeUI;


/**
 * Created On: 20.04.2022<br>
 * Last Edit: 22.03.2023
 *
 * @author Riyufuchi
 * @version 1.5
 * @since 1.0
 */

public class MarvusMain
{
	public static void main(String[] args)
	{
		CustomizeUI.setUI(MarvusConfig.appTheme);
		new DataTableForm(800, 600);
	}
}
