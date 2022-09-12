package gui.windows;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import general.helpers.Helper;
import gui.info.AppFonts;
import gui.info.AppTexts;
import gui.utils.DialogHelper;
import gui.utils.JMenuCreator;
import workData.DataBox;
import workData.Money;

/**
 * Created On: 11.04.2022
 * Last Edit: 12.09.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public final class DataTableForm extends Window
{
	private DataBox<Money> dataBox;
	private JTextField[][] textFields;
	
	public DataTableForm(int width, int height)
	{
		super("Marvus - Datatable", width, height, false, true, true);
		Consumer<Exception> con = e -> {
			DialogHelper.errorDialog(this, e.getMessage(), e.getClass().getSimpleName());
		};
		
		this.dataBox = new DataBox<>(con, AppTexts.DATE_FORMAT_OPTIONS[0]);
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
		if(DialogHelper.yesNoDialog(this, "Exit application?", "Exit confirmation") == 0)
			super.dispose();
	}

	public final void loadData(LinkedList<Money> data) throws NullPointerException, IllegalArgumentException
	{
		if(data == null)
			throw new NullPointerException("Inputed datalist was null");
		if(data.isEmpty())
			throw new IllegalArgumentException("Inputed datalist is emtpy");
		dataBox.setList(data);
		getPane().removeAll();
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
				textFields[x][i].setFont(AppFonts.MAIN);
				textFields[x][i].setBorder(lb);
				pane.add(textFields[x][i], getGBC( i + year, y + 1));
			}
			y++;
		}
		revalidate();
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(AppTexts.DTF_MENU, AppTexts.DTF_MENU_ITEMS, 3);
		jmc.getJMenuBar().setBackground(Color.LIGHT_GRAY);
		JMenuItem[] jmi = jmc.getMenuItem();
		for (int i = 0; i < jmi.length; i++)
		{
			jmi[i].setBackground(Color.LIGHT_GRAY);
			switch (jmi[i].getName())
			{
				case "About" -> jmi[i].addActionListener(event -> about());
				case "Exit" -> jmi[i].addActionListener(event -> onClose());
				case "Export"-> jmi[i].addActionListener(event -> new FileIO(true, this));
				case "Import" -> jmi[i].addActionListener(event -> new FileIO(false, this));
				case "Refresh" -> jmi[i].addActionListener(event -> refresh());
				case "Count" -> jmi[i].addActionListener(event -> new Counter(this));
				case "Date" -> jmi[i].addActionListener(event -> sort());
				case "Preferences" -> jmi[i].addActionListener(event -> new Settings());
				case "Backup" -> jmi[i].addActionListener(event -> Helper.backup(this));
				default -> jmi[i].addActionListener(event -> DialogHelper.informationDialog(this, "This functionality is not implemented yet", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void sort()
	{
		if(dataBox.isEmpty())
		{
			DialogHelper.warningDialog(this, "No data to sort", "Sort error");
			return;
		}
		dataBox.sort();
		refresh();
	}
	
	public DataBox<Money> getDataBox()
	{
		return dataBox;
	}
	
	public void refresh()
	{
		getPane().removeAll();
		try {
			loadData((LinkedList<Money>) dataBox.getList());
		} catch (NullPointerException | IllegalArgumentException e) {
			DialogHelper.errorDialog(this, e.getMessage(), e.getClass().getSimpleName());
			//new ErrorWindow(ErrorCause.INERNAL, e.getMessage());
		}
		repaint();
		revalidate();
	}
	
	private void about()
	{
		new ErrorWindow("About", "This is money manager.\nVersion: " + AppTexts.VERSION + "\nCreated by Riyufuchi.\n"
			+ "Free libs under OpenSource lincention are used (I thnink), however my code is not under OpenSource licention.");
	}
}
