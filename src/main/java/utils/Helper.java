package utils;

import java.awt.Color;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;


/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 01.05.2022
 * 
 * @author Riyufuchi
 * @version 1.2
 * @since 1.0 
 */

public class Helper 
{	
	public static Border defaultTextFieldBorder()
	{
		return new JTextField().getBorder();
	}
	 
	public static void makeBorder(JComponent control, Border border)
	{
		control.setBorder(border);
	}
	
	public static JLabel newLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setFont(Values.FONT_MAIN);
		label.setForeground(Color.LIGHT_GRAY);
		return label;
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