package gui;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import utils.JMenuCreator;
import utils.Values;
import workData.DataBox;
import workData.Money;

/**
 * Created On: 11.04.2022
 * Last Edit: 30.04.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public final class DataTableForm extends Window
{
	private DataBox dataBox;
	private JTextField[][] textFields;
	
	public DataTableForm(int width, int height)
	{
		super("Marvus - Datatable", width, height, false, true, true);
		this.dataBox = new DataBox();
	}

	@Override
	protected void setComponents(JPanel content)
	{
		//content.add(new JButton("Test"), getGBC(0, 0));
		setupJMenu();
	}
	
	@Override
	protected void onClose()
	{
		int confirm = JOptionPane.showOptionDialog(null, "Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0)
		{
			super.dispose();
		}
	}

	public final void loadData(LinkedList<Money> data) throws NullPointerException, IllegalArgumentException
	{
		if(data == null)
			throw new NullPointerException();
		if(data.isEmpty())
			throw new IllegalArgumentException("Inputed datalist is emtpy");
		dataBox.setList(data);
		textFields = new JTextField[data.size()][2];
		String[] listData = data.get(0).getDataArray();
		char oldDate = listData[1].charAt(listData[1].length()-1);
		Iterator<Money> it = data.iterator();
		int year = 0;
		JPanel pane = getPane();
		int y = 0;
		LineBorder lb = new LineBorder(Color.GRAY);
		for(int x = 0; x < textFields.length; x++)
		{
			listData = it.next().getDataArray();
			if(oldDate != listData[1].charAt(listData[1].length() - 1))
			{
				year = year + 2;
				y = 0;
				oldDate = listData[1].charAt(listData[1].length() - 1);
			}
			for(int i = 0; i < textFields[0].length; i++)
			{
				textFields[x][i] = new JTextField(listData[i]);
				//textFields[x][i].setEnabled(false);
				textFields[x][i].setEditable(false);
				textFields[x][i].setBackground(Color.DARK_GRAY);
				textFields[x][i].setForeground(Color.LIGHT_GRAY);
				textFields[x][i].setFont(Values.FONT_MAIN);
				textFields[x][i].setBorder(lb);
				pane.add(textFields[x][i], getGBC( i + year, y + 1));
			}
			y++;
		}
		repaint();
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(Values.DTF_MENU, Values.DTF_MENU_ITEMS, 3);
		jmc.getJMenuBar().setBackground(Color.LIGHT_GRAY);
		JMenuItem[] jmi = jmc.getMenuItem();
		for (int i = 0; i < jmi.length; i++)
		{
			jmi[i].setBackground(Color.LIGHT_GRAY);
			switch (jmi[i].getName())
			{
				case "About" -> jmi[i].addActionListener(event -> about());
				case "Exit" -> jmi[i].addActionListener(event -> System.exit(0));
				case "Export"-> jmi[i].addActionListener(event -> new FileIO(true));
				case "Import" -> jmi[i].addActionListener(event -> new FileIO(false).setImportDestination(this));
				case "Refresh/Load" -> jmi[i].addActionListener(event -> refresh());
				case "Count" -> jmi[i].addActionListener(event -> new Counter(dataBox));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void refresh()
	{
		if(dataBox.isEmpty())
			return;
		dataBox.sort();
		getPane().removeAll();
		loadData((LinkedList<Money>) dataBox.getList());
		repaint();
		revalidate();
	}
	
	private void about()
	{
		new ErrorWindow("About", "This is money manager.\nVersion: " + Values.VERSION + "\nCreated by Riyufuchi.\n"
			+ "Free libs under OpenSource lincention are used (I thnink), however my code is not under OpenSource licention.");
	}
}
