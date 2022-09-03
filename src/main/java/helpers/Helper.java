package helpers;

import java.util.Objects;

/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 04.09.2022
 * 
 * @author Riyufuchi
 * @version 1.4
 * @since 1.0 
 */

public class Helper 
{
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