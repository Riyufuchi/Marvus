package riyufuchi.marvus.app.utils;

import javax.swing.filechooser.FileNameExtensionFilter;

import riyufuchi.sufuLib.enums.AppThemeUI;

public class MarvusConfig 
{
	//Changeable configuration
	public static String workFolder = "data/";
	public static AppThemeUI appTheme = AppThemeUI.NIMBUS_DARK;
	//Unchangeable configuration
	public static final FileNameExtensionFilter MONEY_SUM_FILES = new FileNameExtensionFilter(".ser, .csv, .xml", "ser", "csv", "xml");
	//XML money
	public static final String SUM = "sum";
	public static final String DATE = "date";
	public static final String SUB_ELEMENT = "Money";
	public static final String MAIN_ELEMENT = "MoneyExport";
}
