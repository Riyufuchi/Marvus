package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.FactoryComponent;

/**
 * Created On: 11.04.2022<br>
 * Last Edit: 17.07.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public abstract class Window extends JFrame
{
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;
	
	public Window(String title, int width, int height, boolean alwaysOnTop, boolean resizable, boolean mainWindow)
	{
		this.setTitle(title);
		this.setMinimumSize(new Dimension(width, height));
		this.setLocationRelativeTo(null);
		if(mainWindow)
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		else
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.gbc = new GridBagConstraints();
		this.gbc.fill = GridBagConstraints.HORIZONTAL;
		this.contentPane = FactoryComponent.newPane(new GridBagLayout());

		setComponents(contentPane);

		this.contentPane.revalidate();
		this.scrollPane = new JScrollPane(contentPane);
		this.add(scrollPane);
		this.pack();
		this.setAlwaysOnTop(alwaysOnTop);
		this.setResizable(resizable);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				onClose();
			}
		});
		this.setVisible(true);
	}
	
	/**
	 * Method for creation control components, automatically called
	 * 
	 * @param content JPanel created in constructor
	 */
	protected abstract void setComponents(JPanel content);
	
	protected void onClose()
	{
		this.dispose();
	}
	
	protected void createLabels(String[] texts)
	{
		int max = texts.length;
		for(int i = 0; i < max; i++)
		{
			contentPane.add(FactoryComponent.newLabel(texts[i]), getGBC(0, i));
		}
	}
	
	/**
	 * Set position of the component
	 * 
	 * @param x - column position
	 * @param y - row position
	 * @return modified instance of GridBagConstraints
	 */
	protected final GridBagConstraints getGBC(int x, int y)
	{
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}

	/**
	 * @return the contentPane
	 */
	protected JPanel getPane() {
		return contentPane;
	}
}
