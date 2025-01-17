package riyufuchi.marvus.tabs.toolTabs;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.dialogs.transactions.TransactionDialog;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.utils.BasicTableTab;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.FinancialCategorySafe;
import riyufuchi.marvusLib.database.TransactionTableModel;
import riyufuchi.marvusLib.enums.MarvusAction;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.SufuFilePicker;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.gui.utils.SufuFactory;

public class InvoiceToolTab extends BasicTableTab
{
	private JButton addNewTransactionBtn, exportInvoiceBtn;
	private FinancialCategorySafe cat;
	
	public InvoiceToolTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.addNewTransactionBtn = SufuFactory.newButton("Add new transaction", ect -> onAddNewTransactionEvent());
		this.exportInvoiceBtn = SufuFactory.newButton("Export invoice", evt -> onExportInvoiceEvent());
		addMenuItems(addNewTransactionBtn, exportInvoiceBtn);
	}
	
	private Iterator<FinancialCategorySafe> calculate()
	{
		Dictionary<String, FinancialCategorySafe> categories = new Hashtable<>();
		currDataSet.stream().forEach(transaction -> {
			if ((cat = categories.get(transaction.getCategory())) == null)
				categories.put(transaction.getCategory(), new FinancialCategorySafe(transaction));
			else 
				cat.add(transaction);
		});
		return categories.elements().asIterator();
	}
	
	private void onExportInvoiceEvent()
	{
		LinkedList<String> rows = new LinkedList<String>();
		rows.add(String.join(";", TransactionTableModel.COLUMN_NAMES));
		String separator =  "-".repeat((int) (rows.getFirst().length() * 1.5));
		LinkedList<String> rows2 = new LinkedList<String>();
		BigDecimal in = new BigDecimal(0);
		BigDecimal out = new BigDecimal(0);
		Iterator<FinancialCategorySafe> it = calculate();
		while (it.hasNext())
		{
			cat = it.next();
			switch (cat.getSum().compareTo(BigDecimal.ZERO))
			{
				case 1 -> in = in.add(cat.getSum());
				//case 0 -> { continue; }
				case -1 -> out = out.add(cat.getSum());
			}
			rows2.add(cat.getCategory() + ": " + cat.getSum());
			for (Transaction t : cat)
				rows.add(t.toCSV());
		}
		rows.add(separator);
		for (String s : rows2)
			rows.add(s);
		rows.add(separator);
		rows.add("Capital: " + in.toPlainString());
		rows.add("Spendings: " + out.toPlainString());
		rows.add("Total: " + in.add(out));
		SufuFilePicker sfp =  new SufuFilePicker(targetWindow.getSelf(), MarvusConfig.defaultWorkFile.getAbsolutePath());
		sfp.setAcceptAllFileFilterUsed(false);
		sfp.setFileFilters(MarvusConfig.CSV);
		sfp.showFileCreator().ifPresent(file -> {
			try
			{
				SufuPersistence.saveToCSVtoString(file.getAbsolutePath(), rows);
			}
			catch (NullPointerException | IOException e)
			{
				SufuDialogHelper.exceptionDialog(targetWindow.getSelf(), e);
				return;
			}
			SufuDialogHelper.informationDialog(targetWindow.getSelf(), "Table sucessfully exported into CSV", "CSV export");
		});
	}
	
	private void onAddNewTransactionEvent()
	{
		Transaction t = new TransactionDialog(targetWindow.getSelf(), database, MarvusAction.ADD).showAndGet();
		if (t == null)
			return;
		currDataSet.add(t);
		refresh();
	}
	
	@Override
	public void refresh()
	{
		super.refresh();
	}
}
