package gui.windows;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import general.helpers.Helper;
import gui.info.AppTexts;
import gui.utils.DialogHelper;
import gui.utils.JMenuCreator;
import workData.DataBox;
import workData.Money;

/**
 * Created On: 11.04.2022
 * Last Edit: 07.10.2022
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
		Consumer<Exception> excetionConsumer = e -> {
			DialogHelper.exceptionDialog(this, e);
		};
		this.dataBox = new DataBox<>(excetionConsumer, AppTexts.DATE_FORMAT_OPTIONS[0]);
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
		//LineBorder lb = new LineBorder(Color.GRAY);
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
				//textFields[x][i].setBackground(Color.DARK_GRAY);
				//textFields[x][i].setForeground(Color.LIGHT_GRAY);
				//textFields[x][i].setFont(AppFonts.MAIN);
				//textFields[x][i].setBorder(lb);
				pane.add(textFields[x][i], getGBC( i + year, y + 1));
			}
			y++;
		}
		revalidate();
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(AppTexts.DTF_MENU, AppTexts.DTF_MENU_ITEMS, 3);
		//jmc.getJMenuBar().setBackground(Color.LIGHT_GRAY);
		//JMenuItem[] jmi = jmc.getMenuItem();
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			//jmi[i].setBackground(Color.LIGHT_GRAY);
			switch (jmc.getItemName(i))
			{
				case "About" -> jmc.setItemAction(i, event -> about());
				case "Exit" -> jmc.setItemAction(i, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> new FileIO(true, this));
				case "Import" -> jmc.setItemAction(i, event -> new FileIO(false, this));
				case "Refresh" -> jmc.setItemAction(i,event -> refresh());
				case "Count" -> jmc.setItemAction(i,event -> new Counter(this));
				case "Date" -> jmc.setItemAction(i,event -> sort());
				case "Preferences" -> jmc.setItemAction(i,event -> new Settings());
				case "Backup" -> jmc.setItemAction(i,event -> Helper.backup(this));
				default -> jmc.setItemAction(i, event -> DialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
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
			DialogHelper.exceptionDialog(this, e);
			//DialogHelper.errorDialog(this, e.getMessage(), e.getClass().getSimpleName());
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
