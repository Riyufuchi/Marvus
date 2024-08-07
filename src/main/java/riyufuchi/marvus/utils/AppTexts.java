package riyufuchi.marvus.utils;

public class AppTexts
{
	public static final String VERSION = "2.1.9";
	public static final String APP_NAME =  "Marvus - " + AppTexts.VERSION;
	public static final String[] BUDGET_TABLE_MENU = {"File", "Transaction", "Overview mode", "Data tools", "Application Tools", "Window", "Help"};
	public static final String[] BUDGET_TABLE_MENU_ITEMS = {
			"Open", "Save", "Save As...", "Import", "Export", "Exit", "",
			"Add", "Edit", "Delete", "View", "",
			"Simple month list", "Categorized month list", "Categorized month overview",
			"Categorized year summary", "Earning/Spending summary", "Data summary", "Multi year table", "Timed detail", "Previous mode", "",
			"Sort", "Fix category", "Month budget planning", "Month outcome", "Backup", "",
			"Application manager", "",
			"Preferences", "Fullscreen", "Refresh", "",
			"About", "About SufuLib", "License"};
	// Other
	public static final String ABOUT_MARVUS = "<html>This is finance manager.<br>"
			+ "Version: " + AppTexts.VERSION + "<br>Created by Riyufuchi © 2021 - 2024."
			+ "<br>My code is under respective license.</html>";
	// SEETINGS DIALOG TEXTS
	public static final String[] DATE_FORMAT_OPTIONS = {"dd.MM.yyyy", "MM.dd.yyyy"};
	public static final String[] LOCAL_DATE_TIME_FORMAT_OPTIONS = {"dd.mm.yyyy", "MM.dd.yyyy"};
	public static final String[] COLOR_OPTIONS = {"Theme colored", "Dark mode"};
	public static final String[] THEMES = {"Default", "System matching", "Nimbus", "Dark Nimbus"};
	public static final String[] WINDOW_SIZE = {"Fullscreen", "800x600", "1280x720", "1920x1080"};
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
