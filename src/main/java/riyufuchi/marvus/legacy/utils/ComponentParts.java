package riyufuchi.marvus.legacy.utils;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ComponentParts
{
	public static final Border TEXTFIELD_DEFAULT_BORDER = new JTextField().getBorder();
	public static final Border BORDER_RED = BorderFactory.createLineBorder(new Color(255, 0, 0));
	public static final Border BORDER_GREEN = BorderFactory.createLineBorder(new Color(0, 255, 0));
}
