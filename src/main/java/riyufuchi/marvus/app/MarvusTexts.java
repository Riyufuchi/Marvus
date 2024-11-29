package riyufuchi.marvus.app;

public class MarvusTexts
{
	public static final String VERSION = "2.4.3";
	public static final String APP_NAME =  "Marvus - " + MarvusTexts.VERSION;
	public static final String[] BUDGET_TABLE_MENU = {"File", "Transaction", "Overview mode", "Tools", "Legacy", "Window", "Help"};
	public static final String[] BUDGET_TABLE_MENU_ITEMS = {
			"New", "Open", "Save", "Save As...", "Import", "Export", "Exit", "",
			"Add", "Backup", "Sort", "",
			"Table list", "Categorized month list", "Uncategorized month list",
			"Year summary", "Year overview", "Data summary", "Week detail", "Previous mode", "",
			"Execute quarry", "Application manager", "",
			"Month outcome", "",
			"Preferences", "Fullscreen", "Refresh", "",
			"About", "About SufuLib", "License"};
	public static final String[] GROUP_BY_TIME_DETAIL = {"Name", "Category"}; 
	// OTHER
	public static final String ABOUT_MARVUS = "<html>This is finance manager.<br>"
			+ "Version: " + MarvusTexts.VERSION + "<br>Created by Riyufuchi © 2021 - 2024."
			+ "<br>My code is under respective license.</html>";
	// DIALOG TEXTS
	public static final String[] DATE_FORMAT_OPTIONS = {"dd.MM.yyyy", "MM.dd.yyyy"};
	public static final String[] LOCAL_DATE_TIME_FORMAT_OPTIONS = {"dd.mm.yyyy", "MM.dd.yyyy"};
	public static final String[] COLOR_OPTIONS = {"Theme colored", "Dark mode"};
	public static final String[] THEMES = {"Default", "System matching", "Nimbus", "Dark Nimbus"};
	public static final String[] WINDOW_SIZE = {"Fullscreen", "800x600", "1280x720", "1920x1080"};
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
