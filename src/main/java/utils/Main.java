package utils;

import gui.DataTableForm;
//import gui.ErrorWindow;

/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 20.04.2022
 * 
 * @author Riyufuchi
 * @version 1.0
 * @since 1.0 
 */

public class Main
{	
	public static void main(String[] args) 
	{
		new DataTableForm(800, 600);
		//new ErrorWindow("test", new Money(0.105, " ").getMoneySum().toPlainString());
	}
}