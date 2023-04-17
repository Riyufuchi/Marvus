package riyufuchi.marvus.marvusLib.legacy.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.app.gui.windows.Settings;
import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.MoneySum;
import riyufuchi.marvus.marvusLib.files.Helper;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.ErrorWindow;
import riyufuchi.sufuLib.gui.Window;
import riyufuchi.sufuLib.gui.utils.JMenuCreator;

/**
 * Created On: 11.04.2022
 * Last Edit: 17.04.2023
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public final class DataTableForm extends Window
{
	private DataBox<MoneySum> dataBox;
	private JTextField[][] textFields;
	
	public DataTableForm(int width, int height)
	{
		super("Marvus - Datatable", width, height, false, true, true);
		Consumer<Exception> exceptionConsumer = e -> DialogHelper.exceptionDialog(this, e);
		this.dataBox = new DataBox<>(exceptionConsumer, AppTexts.DATE_FORMAT_OPTIONS[0]);
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

	public final void loadData(LinkedList<MoneySum> data) throws NullPointerException, IllegalArgumentException
	{
		if(data == null)
			throw new NullPointerException("Inputed datalist was null");
		if(data.isEmpty())
			throw new IllegalArgumentException("Inputed datalist is emtpy");
		dataBox.setList(data);
		getPane().removeAll();
		textFields = new JTextField[data.size()][2];
		String[] listData = data.get(0).toString().split(";");
		char oldDate = listData[1].charAt(listData[1].length()-1);
		Iterator<MoneySum> it = data.iterator();
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
				textFields[x][i] = new JTextField(listData[i]);
				//textFields[x][i].setEnabled(false);
				textFields[x][i].setEditable(false);
				//textFields[x][i].setBackground(Color.DARK_GRAY);
				//textFields[x][i].setForeground(Color.LIGHT_GRAY);
				pane.add(textFields[x][i], getGBC( i + year, y + 1));
			}
			y++;
		}
		revalidate();
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(AppTexts.DTF_MENU, AppTexts.DTF_MENU_ITEMS, 3);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				case "About" -> jmc.setItemAction(i, event -> about());
				case "Exit" -> jmc.setItemAction(i, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> exportData());
				case "Import" -> jmc.setItemAction(i, event -> importData());
				case "Refresh" -> jmc.setItemAction(i,event -> refresh());
				case "Count" -> jmc.setItemAction(i,event -> new Counter(this));
				case "Date" -> jmc.setItemAction(i,event -> sort());
				case "Preferences" -> jmc.setItemAction(i,event -> new Settings());
				case "Backup" -> jmc.setItemAction(i,event -> backupData());
				default -> jmc.setItemAction(i, event -> DialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void exportData()
	{
		if(dataBox.isEmpty())
		{
			DialogHelper.warningDialog(this, "No data to export", "No data found");
			return;
		}
		FileIO fio = new FileIO(this, MarvusConfig.workFolder);
		fio.setFileFilter(MarvusConfig.MONEY_SUM_FILES);
		fio.setAcceptAllFileFilterUsed(false);
		fio.saveFile();
	}
	
	private void importData()
	{
		FileIO fio = new FileIO(this, MarvusConfig.workFolder);
		fio.setFileFilter(MarvusConfig.MONEY_SUM_FILES);
		fio.setAcceptAllFileFilterUsed(false);
		fio.loadFile();
	}
	
	private void backupData()
	{
		try
		{
			Helper.backup(this);
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(this, e);
		}
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
	
	public void refresh()
	{
		getPane().removeAll();
		try
		{
			loadData((LinkedList<MoneySum>) dataBox.getList());
		}
		catch (NullPointerException | IllegalArgumentException e)
		{
			DialogHelper.exceptionDialog(this, e);
		}
		repaint();
		revalidate();
	}
	
	private void about()
	{
		new ErrorWindow("About", 600, 300, "Money manager created by Riyufuchi.\nFinal version: 0.1.22\nLegacy update version: 0.0\n"
			+ "This is leagacy functionality.\n It will not be updated anymore probably.\nIt was ment to replace old version that used object DB (JPA), "
			+ "but funtionility become outdated, so rework is needed.\nThis will be part of new app as legacy functionality.");
	}
	
	// GETTERS
	
	public DataBox<MoneySum> getDataBox()
	{
		return dataBox;
	}
}