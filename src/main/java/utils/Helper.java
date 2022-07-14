package utils;

import java.util.Objects;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import gui.ErrorWindow;


/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 14.07.2022
 * 
 * @author Riyufuchi
 * @version 1.3
 * @since 1.0 
 */

public class Helper 
{	
	public static void setUI(int option)
	{
		try
		{
			switch(option)
			{
				case 0 -> UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				case 1 -> UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				case 2 -> {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
					{
						if ("Nimbus".equals(info.getName()))
						{
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				}
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
		{
			new ErrorWindow(ex.getMessage(), ex.getLocalizedMessage());
		}
	}
	
	public static String checkDoubleFormat(String text) throws NullPointerException, IllegalArgumentException
	{
		Objects.requireNonNull(text);
		if(text.isBlank() || !text.matches(".*\\d.*"))
			throw new IllegalArgumentException();
		if(text.contains(","))
			text = text.replace(",", ".");
		char[] number = new char[text.length()];
		boolean dot = false;
		int index = 0;
		for(int i = 0; i < text.length(); i++)
		{
			if(Character.isDigit(text.charAt(i)))
			{
				number[index] = text.charAt(i);
				index++;
			}
			else if (text.charAt(i) == '.')
			{
				if(dot)
					continue;
				dot = true;
				number[index] = text.charAt(i);
				index++;
			}
		}
		return String.valueOf(number).trim();
	}
}