package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.JMenuCreator;
import utils.Values;
import workData.DataBox;
import workData.Money;

/**
 * Created On: 11.04.2022
 * Last Edit: 29.04.2022
 * @author Riyufuchi
 *
 */
@SuppressWarnings("serial")
public class DataTableForm extends Window
{
	private DataBox dataBox;
	private JTextField[][] textFields;
	
	public DataTableForm(int width, int height)
	{
		super("Data table form", width, height, false, true, true);
		this.dataBox = new DataBox();
		//loadData(Persistance.loadFromCVS("data.cvs"));
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
	
	private void sort()
	{
		dataBox.sort();
		getPane().removeAll();
		loadData((LinkedList<Money>) dataBox.getList());
	}

	public final void loadData(LinkedList<Money> data) throws NullPointerException
	{
		if(data == null)
			throw new NullPointerException();
		dataBox.setList(data);
		textFields = new JTextField[data.size()][2];
		String[] listData = data.get(0).getDataArray();
		char oldDate = listData[1].charAt(listData[1].length()-1);
		Iterator<Money> it = data.iterator();
		int year = 0;
		JPanel pane = getPane();
		int y = 0;
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
				textFields[x][i] = new JTextField();
				textFields[x][i].setEnabled(false);
				textFields[x][i].setText(listData[i]);
				textFields[x][i].setFont(Values.FONT_MAIN);
				pane.add(textFields[x][i], getGBC( i + year, y + 1));
			}
			y++;
		}
		repaint();
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(Values.DTF_MENU, Values.DTF_MENU_ITEMS, 3);
		JMenuItem[] jmi = jmc.getMenuItem();
		for (int i = 0; i < jmi.length; i++) {
			switch (jmi[i].getName()) {
			case "About":
				jmi[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						new ErrorWindow("About", "This is money manager.\nVersion: " + Values.VERSION + "\n"
								+ "Created by Riyufuchi.\n"
								+ "Free libs under OpenSource lincention are used (I thnink), however my code is not under OpenSource licention.");
					}
				});
				break;
			case "Exit":
				jmi[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						System.exit(0);
					}
				});
				break;
			case "Export":
				jmi[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						new FileIO(true);
					}
				});
				break;
			case "Import":
				jmi[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						importData();
					}
				});
				break;
		case "Refresh/Load":
			jmi[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					refresh();
				}
			});
			break;
		case "Count":
			jmi[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					count();
				}
			});
			break;
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void count()
	{
		new Counter(dataBox);
	}
	
	private void importData()
	{
		new FileIO(false).setImportDestination(this);
	}
	
	private void refresh()
	{
		sort();
		repaint();
		revalidate();
	}

}
