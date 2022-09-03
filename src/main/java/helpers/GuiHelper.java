package helpers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Created On: 17.07.2022<br>
 * Last Edit: 04.09.2022
 */
public class GuiHelper 
{
	public static int yesNoDialog(JFrame parentFrame, String message, String title)
	{
		return JOptionPane.showOptionDialog(parentFrame, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	}
	
	public static void errorDialog(JFrame parentFrame, String message, String title)
	{
		JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void informationDialog(JFrame parentFrame, String message, String title)
	{
		JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
