package riyufuchi.marvus.app.gui.windows;

import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusData.TransactionCalculations;
import riyufuchi.marvus.marvusData.TransactionComparation;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.ErrorWindow;
import riyufuchi.sufuLib.gui.Window;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;
import riyufuchi.sufuLib.gui.utils.JMenuCreator;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 18.04.2023
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public class BudgetDataTable extends Window
{
	private DataBox<Transaction> dataBox;
	
	public BudgetDataTable()
	{
		super("Marvus - Budget table", 800, 600, false, true, true);
		this.dataBox = new DataBox<>(null, TransactionComparation.byDate());
	}
	
	private void setupJMenu()
	{
		JMenuCreator jmc = new JMenuCreator(AppTexts.BUDGET_TABLE_MENU, AppTexts.BUDGET_TABLE_MENU_ITEMS, 3);
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
				case "Money" -> jmc.setItemAction(i,event -> consumerFunction(TransactionCalculations.icomeToOutcome(4)));
				case "Add" -> jmc.setItemAction(i, event -> new AddTransactionDialog(dataBox).showDialog());
				case "General" -> jmc.setItemAction(i,event -> displayGeneral());
				case "Preferences" -> jmc.setItemAction(i,event -> new Settings());
				//case "Backup" -> jmc.setItemAction(i,event -> backupData());
				default -> jmc.setItemAction(i, event -> DialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	private void consumerFunction(Consumer<DataBox<Transaction>> operation)
	{
		operation.accept(dataBox);
	}
	
	public void refresh()
	{
		getPane().removeAll();
		displayGeneral();
	}
	
	private void displayGeneral()
	{
		JPanel panel = getPane();
		int i = 0;
		for(Transaction t : dataBox)
		{
			panel.add(FactoryComponent.newTextField(t.toString()), getGBC(0, i));
			i++;
		}
		repaint();
	}
	
	private void exportData()
	{
		if(dataBox.isEmpty())
		{
			DialogHelper.warningDialog(this, "No data to export", "No data found");
			return;
		}
		TransactionIO fio = new TransactionIO(this, MarvusConfig.workFolder);
		fio.setFileFilter(MarvusConfig.TRANSACTION_FILES);
		fio.setAcceptAllFileFilterUsed(false);
		fio.saveFile();
	}
	
	private void importData()
	{
		TransactionIO fio = new TransactionIO(this, MarvusConfig.workFolder);
		fio.setFileFilter(MarvusConfig.TRANSACTION_FILES);
		fio.setAcceptAllFileFilterUsed(false);
		fio.loadFile();
	}

	private void about()
	{
		new ErrorWindow("About", "This is budget manager.\nVersion: " + AppTexts.VERSION + "\nCreated by Riyufuchi.\nMy code is under respective licention.");
	}
	
	@Override
	protected void setComponents(JPanel content)
	{
		setupJMenu();
	}
	
	@Override
	protected void onClose()
	{
		if(DialogHelper.yesNoDialog(this, "Exit application?", "Exit confirmation") == 0)
			super.dispose();
	}
	
	public DataBox<Transaction> getDataBox()
	{
		return dataBox;
	}
}
