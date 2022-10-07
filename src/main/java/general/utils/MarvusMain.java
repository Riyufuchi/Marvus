package general.utils;

import gui.utils.CustomizeUI;
import gui.windows.DataTableForm;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 07.10..2022
 * 
 * @author Riyufuchi
 * @version 1.4
 * @since 1.0 
 */

public class MarvusMain
{	
	public static void main(String[] args) 
	{
		CustomizeUI.setUI(3);
		new DataTableForm(800, 600);
	}
}