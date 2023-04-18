package riyufuchi.marvus.app.utils;

public class AppTexts
{
	public static final String VERSION = "0.1.23";
	public static final String[] BUDGET_TABLE_MENU = {"File", "Transaction", "Budget overview", "Window", "Help"};
	public static final String[] BUDGET_TABLE_MENU_ITEMS = {
			"Backup", "Export", "Import", "Exit", "",
			"Add", "Edit", "Delete", "",
			"General", "Money", "",
			"Preferences", "",
			"About"};
	// SEETINGS DIALOG TEXTS
	public static final String[] DATE_FORMAT_OPTIONS = {"dd.MM.yyyy", "MM.dd.yyyy"};
	public static final String[] LOCAL_DATE_TIME_FORMAT_OPTIONS = {"dd.mm.yyyy", "MM.dd.yyyy"};
	public static final String[] COLOR_OPTIONS = {"Theme colored", "Dark mode"};
	public static final String[] THEMES = {"Default", "System matching", "Nimbus", "Dark Nimbus"};
	
	// LEGACY TEXT
	public static final String[] DTF_MENU = {"File", "MoneySum", "Sort by", "Window", "Help"};
	public static final String[] DTF_MENU_ITEMS = {
			"Backup", "Export", "Import", "Exit", "",
			"Count", "Add", "Edit", "Delete", "Refresh", "",
			"Date", "Hightest to lowest", "", "Preferences", "",
			"About"};
	public static final String[] FIO_LABELS = {"File type:", "Path type:", "File name:", "Actual path:"};
	public static final String[][] FIO_COMBO_BOX_ITEMS = {  {".csv", ".xml", ".dat"},
															{"Default", "Custom"}};
}
