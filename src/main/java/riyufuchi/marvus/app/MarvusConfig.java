package riyufuchi.marvus.app;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import riyufuchi.sufuLib.enums.SufuAppTheme;

public class MarvusConfig
{
	// Changeable configuration - Default settings
	public static boolean fullscreen = false;
	public static int width = 800;
	public static int height = 600;
	public static String workFolder = "marvusData/";
	public static SufuAppTheme appTheme = SufuAppTheme.CROSS_PLATFORM;
	public static int dateFormatIndex = 0;
	public static File defaultWorkFile = new File(workFolder + "database.marvusdb");
	public static boolean showQuitDialog = true;
	public static int currentFinancialYear = 0;
	public static boolean autoLoadData = false;
	public static boolean autoMaximize = false;
	// Unchangeable configuration
	public static final File DEFAULT_WORK_FILE = new File(workFolder + "database.marvusdb");
	public static final String ENUM_FOLDER = "enums/";
	public static final String MDB_EXT = ".marvusdb";
	public static final String SETTINGS_FILE_PATH = workFolder + "config.conf";
	public static final String ENTITY_FILE_PATH = workFolder + ENUM_FOLDER + "entities.csv";
	public static final String CATEGORY_FILE_PATH = workFolder + ENUM_FOLDER + "categories.csv";
	public static final String TRANSACTION_MACRO_FILE_PATH = workFolder + ENUM_FOLDER + "transactionMacros.csv";
	// Tables
	public static final String ENTITY_TABLE_PATH = workFolder + ENUM_FOLDER + "entities.marvusTable";
	public static final String TRANSACTION_MACRO_TABLE_PATH = workFolder + ENUM_FOLDER + "transactionMacros.marvusTable";
	public static final String CATEGORY_TABLE_PATH = workFolder + ENUM_FOLDER + "categories.marvusTable";
	// Filters
	public static final FileNameExtensionFilter SER = new FileNameExtensionFilter(".ser", "ser");
	public static final FileNameExtensionFilter CSV = new FileNameExtensionFilter(".csv", "csv");
	public static final FileNameExtensionFilter XML = new FileNameExtensionFilter(".xml", "xml");
	public static final FileNameExtensionFilter MDB = new FileNameExtensionFilter(MDB_EXT, MDB_EXT.substring(1));
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
