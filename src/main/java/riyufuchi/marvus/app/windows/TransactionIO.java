package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 27.03.2023<br>
 * Last Edit: 10.05.2023
 * 
 * @author Riyufuchi
 */
public class TransactionIO extends SufuFileChooser
{
	private BudgetDataTable budgetDataTable;
	
	public TransactionIO(BudgetDataTable budgetDataTable, String filePath)
	{
		super(filePath);
		this.budgetDataTable = budgetDataTable;
	}

	@Override
	protected void onSave(String path)
	{
		if(!path.contains("."))
		{
			path += getFileFilter().getDescription();
		}
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				try
				{
					Persistance.<Transaction>saveToCSV(path, budgetDataTable.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					DialogHelper.exceptionDialog(budgetDataTable, e);
				}
			}
			case ".ser" -> {
				try
				{
					Persistance.serialize(path, budgetDataTable.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					DialogHelper.exceptionDialog(budgetDataTable, e);
				}
			}
			default -> DialogHelper.errorDialog(budgetDataTable, "File is missing an extension or extension was not recognized\n" + "Extension: " + extension, "Extension not recognized");
		}
	}

	@Override
	protected void onLoad(String path)
	{
		if(!path.contains("."))
		{
			path += getFileFilter().getDescription();
		}
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				loadCSV(path);
			}
			case ".ser" -> {
				try
				{
					budgetDataTable.getDataBox().setList((LinkedList<Transaction>)Persistance.<Transaction>deserialize(path));
				}
				catch (NullPointerException | ClassNotFoundException | IOException e)
				{
					DialogHelper.exceptionDialog(budgetDataTable, e);
				}
			}
			default -> DialogHelper.errorDialog(budgetDataTable, "File is missing an extension or extension was not recognized\n" + "Extension: " + extension, "Extension not recognized");
		}
		budgetDataTable.refresh();
	}
	
	private void loadCSV(String path)
	{
		List<String> list = null;
		LinkedList<Transaction> l = new LinkedList<>();
		String[] split = null;
		try
		{
			list = Persistance.loadFromCSV(path);
			Iterator<String> it = list.iterator();
			while(it.hasNext())
			{
				split = it.next().split(";");
				l.add(new Transaction(split[0], split[1], split[2]));
			}
		}
		catch (NullPointerException | IOException | IndexOutOfBoundsException e)
		{
			DialogHelper.exceptionDialog(budgetDataTable, e);
		}
		budgetDataTable.getDataBox().setList(l);
	}
}
