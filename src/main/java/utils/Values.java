package utils;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.border.Border;

public class Values 
{
	public static final String VERSION = "0.0.1";
	//Window contents
	public static final String[] DTF_MENU = {"File", "Data", "Sort by", "Window", "Help"};
	public static final String[] DTF_MENU_ITEMS = {"Create", "Export", "Import", "Exit", "", "Count", "Add", "Edit", "Delete", "Refresh", "", "Date", "Hightest to lowest", "", "Preferences", "", "About"};
	public static final String[] FIO_LABELS = {"File type:", "Path type:", "File name:", "Actual path:"};
	public static final String[][] FIO_COMBO_BOX_ITEMS = {  {".csv", ".xml", ".dat"},
															{"Default", "Custom"}};
	//Visuals
	public static final Color DEFAULT_BUTTON_BACKGROUND = new Color(214,217,223);
	//public static final Color DEFAULT_PANE_BACKGROUND = new Color(192,192,192);
	public static final Color DEFAULT_PANE_BACKGROUND = new Color(64,64,64);
	//Borders
	public static final Border TEXTFIELD_DEFAULT_BORDER = new JTextField().getBorder();
	//Fonts
	public static final Font FONT_MAIN = new Font("Dialog.plain", Font.BOLD, 18);
	public static final Font FONT_LABEL = javax.swing.UIManager.getDefaults().getFont("Label.font");
	//XML money
	public static final String SUM = "sum";
	public static final String DATE = "date";
	public static final String SUB_ELEMENT = "Money";
	public static final String MAIN_ELEMENT = "MoneyExport";
	//Dynamics
	public static int themeID = 0;
	public static boolean darkBackground = false;
	
}
