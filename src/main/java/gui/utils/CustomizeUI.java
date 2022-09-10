package gui.utils;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import general.utils.Values;
import gui.info.AppColors;
import gui.info.AppFonts;
import gui.windows.ErrorWindow;

/**
 * Created On: 04.09.2022<br>
 * Last Edit: 10.09.2022
 */
public class CustomizeUI
{
	private static void setFont()
	{
		UIManager.getLookAndFeelDefaults().put("Button.font", AppFonts.MAIN);
		UIManager.getLookAndFeelDefaults().put("TextField.font", AppFonts.MAIN);
		UIManager.getLookAndFeelDefaults().put("ComboBox.font", AppFonts.MAIN);
		UIManager.getLookAndFeelDefaults().put("Label.font", AppFonts.MAIN);
		UIManager.getLookAndFeelDefaults().put("OptionPane.messageFont", AppFonts.MAIN_SMALL);
		UIManager.getLookAndFeelDefaults().put("Menu.font", AppFonts.MAIN_TINY);
		UIManager.getLookAndFeelDefaults().put("MenuItem.font", AppFonts.MAIN_TINY);
	}
	
	private static void setDarkNimbus()
	{
		UIManager.put("nimbusBase", AppColors.DEFAULT_PANE_BACKGROUND);
		//Foregrounds
		//UIManager.put("Button.foreground", Values.LIGHT_LABEL);
		UIManager.put("OptionPane.messageForeground", AppColors.LIGHT_LABEL);
		UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", Color.BLACK);
		//Backgrounds
		UIManager.put("TextField.background", AppColors.LIGHT_LABEL);
		UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\".background", AppColors.LIGHT_LABEL);
		UIManager.getLookAndFeelDefaults().put("background", AppColors.DEFAULT_PANE_BACKGROUND);
	}
	
	private static void setNimbusTheme() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
		{
			if ("Nimbus".equals(info.getName()))
			{
				UIManager.setLookAndFeel(info.getClassName());
				setFont();
				break;
			}
		}
	}
	
	public static void setUI(int option)
	{
		try
		{
			switch(option)
			{
				case 0 -> {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					setFont();
				}
				case 1 -> {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					setFont();
				}
				case 2 -> {
					setNimbusTheme();
				}
				case 3 -> {
					setNimbusTheme();
					setDarkNimbus();
					//UIManager.put("nimbusBlueGrey", Values.DEFAULT_PANE_BACKGROUND);
					//UIManager.put("control", Values.DEFAULT_BUTTON_BACKGROUND);
					//UIManager.getLookAndFeelDefaults().put("text", Values.LIGHT_LABEL);
					//UIManager.put("Label[Enabled].foreground", Color.LIGHT_GRAY);
					//UIManager.getLookAndFeelDefaults().put("foreground", Color.LIGHT_GRAY);
					//UIManager.getLookAndFeelDefaults().put("Button[Enabled].foreground", Color.LIGHT_GRAY);
				}
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
		{
			new ErrorWindow(ex.getMessage(), ex.getLocalizedMessage());
		}
		Values.themeID = option;
	}
}
