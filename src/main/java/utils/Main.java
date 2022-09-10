package utils;

import gui.DataTableForm;
import helpers.CustomizeUI;

/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 10.09.2022
 * 
 * @author Riyufuchi
 * @version 1.3
 * @since 1.0 
 */

public class Main
{	
	public static void main(String[] args) 
	{
		CustomizeUI.setUI(3);
		new DataTableForm(800, 600);
	}
}