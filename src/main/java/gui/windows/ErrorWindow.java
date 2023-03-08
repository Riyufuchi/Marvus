package gui.windows;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import gui.info.AppColors;
import gui.info.AppFonts;

/**
 * Created On: 13.07.2020
 * Last Edit: 10.09.2022
 * 
 * @author Riyufuchi
 * @version 1.6
 * @since 1.0
 */

@SuppressWarnings("serial")
public class ErrorWindow extends Window
{
	private JTextArea errorMessageLabel;
	
	@Deprecated
	public ErrorWindow(ErrorCause error, String errorMessage)
	{
		super(error.text, 400, 300, false, true, false);
		this.errorMessageLabel.setText(errorMessage);
	}
	
	public ErrorWindow(String title, String errorMessage)
	{
		super(title, 400, 300, false, true, false);
		this.errorMessageLabel.setText(errorMessage);
	}
	
	@Override
	protected void setComponents(JPanel content)
	{
		errorMessageLabel = new JTextArea();
		errorMessageLabel.setEditable(false);
		errorMessageLabel.setLineWrap(true);
		errorMessageLabel.setWrapStyleWord(true);
		errorMessageLabel.setBackground(AppColors.DEFAULT_PANE_BACKGROUND);
		errorMessageLabel.setForeground(Color.LIGHT_GRAY);
		errorMessageLabel.setFont(AppFonts.MAIN);
		super.addComponentListener(new ComponentAdapter() 
		{
			public void componentResized(ComponentEvent componentEvent) 
			{
				resize();
			}
		});
		content.add(errorMessageLabel, getGBC(0,0));
	}
	
	private void resize()
	{
		errorMessageLabel.setBounds(0, 0, (int)(getWidth() * 0.90), getHeight());
		this.repaint();
	}
}