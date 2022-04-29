package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.Values;


/**
 * Created On: 11.04.2022
 * Last Edit: 11.04.2022
 * @author riyufuchi
 *
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
		this.contentPane = new JPanel(null);
		this.getPane().setBackground(Values.DEFAULT_PANE_BACKGROUND);
		this.getPane().setLayout(new GridBagLayout());

		setComponents(getPane());

		this.getPane().revalidate();
		this.scrollPane = new JScrollPane(getPane());
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
	
	protected GridBagConstraints getGBC(int x, int y)
	{
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}

	/**
	 * @return the contentPane
	 */
	public JPanel getPane() {
		return contentPane;
	}
}
