package gui;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import utils.Values;

/**
 * Created On: 13.07.2020
 * Last Edit: 30.04.2022
 * 
 * @author Riyufuchi
 * @version 1.3
 * @since 1.0
 */

@SuppressWarnings("serial")
public class ErrorWindow extends Window
{
	private JTextArea errorMessageLabel;
	
	public ErrorWindow(String title, String errorMessage)
	{
		super(title, 400, 300, true, true, false);
		this.errorMessageLabel.setText(errorMessage);
		super.addComponentListener(new ComponentAdapter() 
		{
			public void componentResized(ComponentEvent componentEvent) 
			{
				resize();
			}
		});
	}

	public void componentResized(ComponentEvent componentEvent) 
	{
		errorMessageLabel.setBounds(0, 0, (int)(getWidth() * 0.90), getHeight());
		this.repaint();
	}
	
	@Override
	protected void setComponents(JPanel content)
	{
		errorMessageLabel = new JTextArea();
		errorMessageLabel.setEditable(false);
		errorMessageLabel.setLineWrap(true);
		errorMessageLabel.setWrapStyleWord(true);
		errorMessageLabel.setBackground(Values.DEFAULT_PANE_BACKGROUND);
		errorMessageLabel.setForeground(Color.LIGHT_GRAY);
		errorMessageLabel.setFont(Values.FONT_MAIN);
		content.add(errorMessageLabel, getGBC(0,0));
	}
	
	private void resize()
	{
		errorMessageLabel.setBounds(0, 0, (int)(getWidth() * 0.90), getHeight());
		this.repaint();
	}
}