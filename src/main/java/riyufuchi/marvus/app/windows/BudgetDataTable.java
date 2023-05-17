package riyufuchi.marvus.app.windows;

import java.util.Comparator;
import java.util.function.Consumer;

import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.DataDisplay;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusData.TransactionCalculations;
import riyufuchi.marvus.marvusData.TransactionComparation;
import riyufuchi.marvus.marvusData.TransactionComparation.CompareMethod;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.ErrorWindow;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.gui.SufuMenuCreator;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 16.05.2023
 * 
 * @author Riyufuchi
 */
public class BudgetDataTable extends SufuWindow
{
	private DataBox<Transaction> dataBox;
	private Consumer<DataBox<Transaction>> displayMode;
	
	public BudgetDataTable()
	{
		super("Marvus - Budget table", 800, 600, false, true, true);
		this.dataBox = new DataBox<>(e -> DialogHelper.exceptionDialog(this, e), TransactionComparation.compareBy(CompareMethod.OldestToNewest));
		this.displayMode = DataDisplay.simpleList(this);
	}
	
	private void setupJMenu()
	{
		SufuMenuCreator jmc = new SufuMenuCreator(AppTexts.BUDGET_TABLE_MENU, AppTexts.BUDGET_TABLE_MENU_ITEMS, 3);
		final int max = jmc.getNumberOfMenuItems();
		for (int i = 0; i < max; i++)
		{
			switch (jmc.getItemName(i))
			{
				// Basic
				case "About" -> jmc.setItemAction(i, event -> about());
				case "Exit" -> jmc.setItemAction(i, event -> onClose());
				case "Export"-> jmc.setItemAction(i,event -> exportData());
				case "Import" -> jmc.setItemAction(i, event -> importData());
				case "Refresh" -> jmc.setItemAction(i,event -> refresh());
				// Order
				case "Sort" -> jmc.setItemAction(i, e -> sortData(TransactionComparation.compareBy(DialogHelper.<CompareMethod>optionDialog(this, "Choose sorting method", "Sorting method chooser", CompareMethod.values()))));
				// Operations
				case "Income to outcome" -> jmc.setItemAction(i,event -> setConsumerFunction(TransactionCalculations.incomeToOutcome(DateUtils.showMonthChooser(this).getValue())));
				// Data handling
				case "Add" -> jmc.setItemAction(i, event -> new AddDialog(this).showDialog());
				// Display modes
				case "Simple list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(DataDisplay.simpleOrderableList(this)));
				case "Category list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(DataDisplay.categoryListByMonth(this)));
				case "Month list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(DataDisplay.monthList(this)));
				case "Year list" -> jmc.setItemAction(i,event -> updateDataDisplayMode(DataDisplay.yearOverview(this)));
				// Other
				case "Preferences" -> jmc.setItemAction(i,event -> new SettingsDialog(this).showDialog());
				//case "Backup" -> jmc.setItemAction(i,event -> backupData());
				default -> jmc.setItemAction(i, event -> DialogHelper.informationDialog(this, "This functionality haven't been implemented yet.", "Info"));
			}
		}
		super.setJMenuBar(jmc.getJMenuBar());
	}
	
	// Delegations
	
	private void sortData(Comparator<Transaction> comp)
	{
		dataBox.setComparator(comp);
		displayMode = DataDisplay.simpleOrderableList(this);
		dataBox.sort();
		refresh();
	}
	
	private TransactionIO createTransactionIO()
	{
		TransactionIO fio = new TransactionIO(this, MarvusConfig.workFolder);
		fio.setFileFilters(MarvusConfig.SER, MarvusConfig.CSV);
		return fio;
	}
	
	private void exportData()
	{
		if(dataBox.isEmpty())
		{
			DialogHelper.warningDialog(this, "No data to export", "No data found");
			return;
		}
		TransactionIO fio = createTransactionIO();
		fio.setAcceptAllFileFilterUsed(false);
		fio.showSaveChooser();
	}
	
	private void importData()
	{
		createTransactionIO().showLoadChooser();
		//fio.loadFile();
	}

	private void about()
	{
		new ErrorWindow("About", "This is budget manager.\nVersion: " + AppTexts.VERSION + "\nCreated by Riyufuchi.\nMy code is under respective licention.");
	}
	
	private void displayData()
	{
		displayMode.accept(dataBox);
		repaint();
		revalidate();
	}
	
	private void updateDataDisplayMode(Consumer<DataBox<Transaction>> dataDisplayMode)
	{
		displayMode = dataDisplayMode;
		refresh();
	}
	
	/**
	 * Refresh displayed data
	 */
	public void refresh()
	{
		if(dataBox.isEmpty())
		{
			DialogHelper.warningDialog(this, "No data to display", "No data found");
			return;
		}
		getPane().removeAll();
		displayData();
	}
	
	// OnEvent
	
	@Override
	protected void onClose()
	{
		if(DialogHelper.yesNoDialog(this, "Exit application?", "Exit confirmation") == 0)
			super.dispose();
	}
	
	// Setters
	
	private void setConsumerFunction(Consumer<DataBox<Transaction>> operation)
	{
		if(dataBox.isEmpty())
		{
			DialogHelper.warningDialog(this, "No data to work with", "No data found");
			return;
		}
		operation.accept(dataBox);
	}
	
	@Override
	protected void setComponents(JPanel content)
	{
		setupJMenu();
	}
	
	// Getters
	
	public DataBox<Transaction> getDataBox()
	{
		return dataBox;
	}
}
