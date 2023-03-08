package gui.utils;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Created On: 17.07.2022<br>
 * Last Edit: 07.10.2022
 */
public class DialogHelper 
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
	
	public static void warningDialog(JFrame parentFrame, String message, String title)
	{
		JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void exceptionDialog(JFrame parentFrame, Exception exception)
	{
		JOptionPane.showMessageDialog(parentFrame, exception.getMessage(), exception.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static Enum enumDialog(JFrame parentFrame, String message, String title, Enum[] enumeration)
	{
		JComboBox<Enum> enumBox = FactoryComponent.<Enum>createCombobox(enumeration);
		final JComponent[] inputs = new JComponent[] { new JLabel(message), enumBox};
		int result = JOptionPane.showConfirmDialog(null, inputs, title, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION)
			return enumBox.getItemAt(enumBox.getSelectedIndex());
		return null;
	}
}
