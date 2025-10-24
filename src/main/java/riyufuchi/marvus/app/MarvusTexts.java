package riyufuchi.marvus.app;

import riyufuchi.marvusLib.records.MarvusComboBoxDialogTexts;

public class MarvusTexts
{
	public static final String VERSION = "2.7.0 - beta 5";
	public static final String APP_NAME =  "Marvus - " + VERSION;
	// MENU BAR
	public static final String[] BUDGET_TABLE_MENU = {"File", "Transaction", "Overview mode", "Tools", "Legacy", "Window", "Help"};
	public static final String[] FILE = {"New", "Open", "Save", "Save As...", "Import", "Export", "Exit"};
	public static final String[] TRANSACTION = {"Add", "Backup"};
	public static final String[] OVERVIEW_MODES = {"Table list", "Categorized month list", "Uncategorized month list",
			"Year summary", "Year overview", "Data summary", "Week detail", "Previous mode"};
	public static final String[] TOOLS = {"Execute quarry", "Entity manager", "Create invoice"};
	public static final String[] LEGACY = {"Month outcome"};
	public static final String[] WINDOW = {"Preferences", "Fullscreen", "Refresh"};
	public static final String[] HELP = {"About", "About SufuLib", "About MarvusLib", "License"};
	public static final String[][] BUDGET_TABLE_MENU_ITEMS = {FILE, TRANSACTION, OVERVIEW_MODES, TOOLS, LEGACY, WINDOW, HELP};
	// COMBO BOXES OPTION 
	public static final String[] GROUP_BY_TIME_DETAIL = {"Name", "Category"}; 
	// DIALOG TEXTS
	public static final String ABOUT_MARVUS = "<html>This is finance manager.<br>"
			+ "Version: " + VERSION + "<br>Created by Riyufuchi © 2021 - 2025."
			+ "<br>My code is under respective license.</html>";
	public static final MarvusComboBoxDialogTexts CATEGORY_DIALOG_TEXT = new MarvusComboBoxDialogTexts("Category name operation", "Old category:", "New category:");
	public static final MarvusComboBoxDialogTexts NAME_DIALOG_TEXT = new MarvusComboBoxDialogTexts("Entity name operation", "Old name:", "New name:");
	// DIALOG TEXTS
	public static final String[] DATE_FORMAT_OPTIONS = {"dd.MM.yyyy", "MM.dd.yyyy"};
	public static final String[] LOCAL_DATE_TIME_FORMAT_OPTIONS = {"dd.mm.yyyy", "MM.dd.yyyy"};
	public static final String[] COLOR_OPTIONS = {"Theme colored", "Dark mode"};
	public static final String[] THEMES = {"Default", "System matching", "Nimbus", "Dark Nimbus"};
	public static final String[] WINDOW_SIZE = {"Fullscreen", "800x600", "1280x720", "1366x768", "1920x1080", "2560x1440"};
	// TABS TEXTS
	public static final String[] TRANSACTION_VIEWER_HEADER = {"ID", "Name", "Category", "Value", "Currency", "Date", "Note"};
	public static final String[] VALUE_OPTIONS = {"All", "Income only", "Spendings only"};
	public static final String[] NOTE_OPTIONS = {"Note only", "Without note only"};
	// LEGACY TEXT
	public static final String[] DTF_MENU = {"File", "MoneySum", "Sort by", "Window", "Help"};
	public static final String[] DTF_MENU_ITEMS = {
			"Backup", "Export", "Import", "Exit", "",
			"Count", "Add", "Edit", "Delete", "",
			"Date", "Hightest to lowest", "",
			"Preferences", "Refresh", "",
			"About", "About SufuLib"};
	public static final String[] FIO_LABELS = {"File type:", "Path type:", "File name:", "Actual path:"};
	public static final String[][] FIO_COMBO_BOX_ITEMS = {  {".csv", ".xml", ".dat"},
															{"Default", "Custom"}};
}
