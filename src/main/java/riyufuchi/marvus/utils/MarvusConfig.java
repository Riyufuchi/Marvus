package riyufuchi.marvus.utils;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import riyufuchi.sufuLib.enums.AppTheme;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;

public class MarvusConfig
{
	// Changeable configuration - Default settings
	public static boolean fullscreen = false;
	public static int width = 800;
	public static int height = 600;
	public static String workFolder = "appdata/";
	public static AppTheme appTheme = AppTheme.CROSS_PLATFORM;
	public static int dateFormatIndex = 0;
	public static File currentWorkFile = new File(workFolder + "data.csv");
	public static boolean showQuitDialog = true;
	public static int financialYear = SufuDateUtils.toLocalDateTime(SufuDateUtils.nowDateString()).getYear(); // TODO: Let user set this value
	public static boolean autoLoadData = false;
	// Unchangeable configuration
	public static final FileNameExtensionFilter SER = new FileNameExtensionFilter(".ser", "ser");
	public static final FileNameExtensionFilter CSV = new FileNameExtensionFilter(".csv", "csv");
	public static final FileNameExtensionFilter XML = new FileNameExtensionFilter(".xml", "xml");
	public static final FileNameExtensionFilter MDB = new FileNameExtensionFilter(".dat", "dat");
	public static final String SETTINGS_FILE_PATH = workFolder + "config.txt";
	public static final String CATEGORY_FILE_PATH = workFolder + "category.txt";
	public static final String TRANSACTION_MACRO_FILE_PATH = workFolder + "transactionsMacro.txt";
	// XML money
	public static final String NAME = "name";
	public static final String CATEGORY = "category";
	public static final String VALUE = "value";
	public static final String CURRENCY = "currency";
	public static final String DATE = "date";
	public static final String NOTE = "note";
	public static final String SUB_ELEMENT = "Transaction";
	public static final String MAIN_ELEMENT = "Transactions";
}
