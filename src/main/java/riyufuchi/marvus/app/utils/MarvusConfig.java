package riyufuchi.marvus.app.utils;

import javax.swing.filechooser.FileNameExtensionFilter;

import riyufuchi.sufuLib.enums.AppThemeUI;

public class MarvusConfig
{
	//Changeable configuration
	public static String workFolder = "data/";
	public static AppThemeUI appTheme = AppThemeUI.CROSS_PLATFORM; // Default value - changed when configuration file exists
	public static int dateFormatIndex = 0;
	//Unchangeable configuration
	public static final FileNameExtensionFilter MONEY_SUM_FILES = new FileNameExtensionFilter(".ser, .csv, .xml", "ser", "csv", "xml");
	public static final FileNameExtensionFilter TRANSACTION_FILES = new FileNameExtensionFilter(".ser, .csv", "ser", "csv");
	public static final FileNameExtensionFilter SER = new FileNameExtensionFilter(".ser", "ser");
	public static final FileNameExtensionFilter CSV = new FileNameExtensionFilter(".csv", "csv");
	public static final String SETTINGS_FILE_PATH = "data/config.txt";
	//XML money
	public static final String SUM = "sum";
	public static final String DATE = "date";
	public static final String SUB_ELEMENT = "Money";
	public static final String MAIN_ELEMENT = "MoneyExport";
}
