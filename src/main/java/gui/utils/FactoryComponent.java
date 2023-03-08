package gui.utils;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import general.utils.Values;
import gui.info.AppColors;
import gui.info.AppFonts;

public class FactoryComponent
{
	public static JButton createButton(String text, ActionListener al)
	{
		JButton button = new JButton(text);
		if(Values.themeID == 0)
			button.setBackground(AppColors.DEFAULT_BUTTON_BACKGROUND);
		//button.setFont(Values.FONT_MAIN);
		button.addActionListener(al);
		return button;
	}
	
	public static <E> JComboBox<E> createCombobox(E[] items)
	{
		JComboBox<E> comboBox = new JComboBox<>(items);
		if(Values.themeID == 0)
			comboBox.setBackground(AppColors.DEFAULT_BUTTON_BACKGROUND);
		//comboBox.setFont(Values.FONT_MAIN);
		return comboBox;
	}
	
	public static JLabel newLabel(String text)
	{
		JLabel label = new JLabel(text);
		if(Values.themeID == 0 || Values.darkBackground || Values.themeID == 3)
			label.setForeground(Color.LIGHT_GRAY);
		//label.setFont(Values.FONT_MAIN);
		return label;
	}
	
	public static JPanel newPane(LayoutManager layout)
	{
		JPanel pane = new JPanel(null);
		if(Values.themeID == 0 || Values.darkBackground)
			pane.setBackground(AppColors.DEFAULT_PANE_BACKGROUND);
		pane.setLayout(layout);
		return pane;
	}
	
	public static JTextField newTextField(String defaultText)
	{
		JTextField textField = new JTextField(defaultText);
		//textField.setFont(Values.FONT_MAIN);
		return textField;
	}
	
	
	public static JTextArea newTextArea(String text)
	{
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(text);
		//textArea.setBackground(AppColors.DEFAULT_PANE_BACKGROUND);
		//textArea.setForeground(Color.LIGHT_GRAY);
		//textArea.setFont(AppFonts.MAIN);
		return textArea;
	}
}
