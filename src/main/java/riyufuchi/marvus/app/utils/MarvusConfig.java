package riyufuchi.marvus.app.utils;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import riyufuchi.sufuLib.enums.AppThemeUI;

public class MarvusConfig
{
	// Changeable configuration - Default settings
	public static boolean fullscreen = false;
	public static int width = 800;
	public static int height = 600;
	public static String workFolder = "appdata/";
	public static AppThemeUI appTheme = AppThemeUI.CROSS_PLATFORM;
	public static int dateFormatIndex = 0;
	public static File currentWorkFile = null;
	public static boolean showQuitDialog = true;
	// Unchangeable configuration
	public static final FileNameExtensionFilter SER = new FileNameExtensionFilter(".ser", "ser");
	public static final FileNameExtensionFilter CSV = new FileNameExtensionFilter(".csv", "csv");
	public static final FileNameExtensionFilter XML = new FileNameExtensionFilter(".xml", "xml");
	public static final String SETTINGS_FILE_PATH = workFolder + "config.txt";
	// XML money
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String DATE = "date";
	public static final String NOTE = "note";
	public static final String SUB_ELEMENT = "Transaction";
	public static final String MAIN_ELEMENT = "Transactions";
	//public static final String SUB_ELEMENT = "Money";
	//public static final String MAIN_ELEMENT = "MoneyExport";
}
