package utils;

import gui.DataTableForm;
import helpers.CustomizeUI;

/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 04.09.2022
 * 
 * @author Riyufuchi
 * @version 1.2
 * @since 1.0 
 */

public class Main
{	
	public static void main(String[] args) 
	{
		CustomizeUI.setUI(Values.themeID = 3);
		new DataTableForm(800, 600);
	}
}