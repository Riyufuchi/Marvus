package utils;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class FactoryComponent
{
	public static JButton createButton(String text, ActionListener al)
	{
		JButton button = new JButton(text);
		button.setFont(Values.FONT_MAIN);
		button.setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
		button.addActionListener(al);
		return button;
	}
	
	public static <E> JComboBox<E> createCombobox()
	{
		JComboBox<E> comboBox = new JComboBox<>();
		comboBox.setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
		comboBox.setFont(Values.FONT_MAIN);
		return comboBox;
	}
	
	public static JLabel newLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setFont(Values.FONT_MAIN);
		label.setForeground(Color.LIGHT_GRAY);
		return label;
	}
}
