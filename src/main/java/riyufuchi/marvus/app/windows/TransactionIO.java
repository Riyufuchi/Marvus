package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.LinkedList;

import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusData.TransactionXML;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;

/**
 * Created On: 27.03.2023<br>
 * Last Edit: 20.08.2023
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
		path = addExtension(path);
		String extension = path.substring(path.lastIndexOf('.'));
		if (!SufuFileHelper.overwriteProtection(path))
			return;
		switch(extension)
		{
			case ".csv" -> {
				try
				{
					SufuPersistence.<Transaction>saveToCSV(path, budgetDataTable.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					DialogHelper.exceptionDialog(budgetDataTable, e);
					return;
				}
			}
			case ".xml" -> {
				TransactionXML xml = new TransactionXML(path);
				xml.exportXML(budgetDataTable.getDataBox().getList());
			}
			case ".ser" -> {
				try
				{
					SufuPersistence.<Transaction>serialize(path, budgetDataTable.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					DialogHelper.exceptionDialog(budgetDataTable, e);
					return;
				}
			}
			default -> {
				DialogHelper.errorDialog(budgetDataTable, "File is missing an extension or extension was not recognized\n" + "Extension: " + extension, "Extension not recognized");
				return;
			}
		}
		DialogHelper.informationDialog(budgetDataTable, "Succesfuly saved to:\n" + path, "Save progress");
	}

	@Override
	protected void onLoad(String path)
	{
		path = addExtension(path);
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				loadCSV(path);
			}
			case ".xml" -> {
				TransactionXML xml = new TransactionXML(path);
				xml.parseTransaction();
				budgetDataTable.getDataBox().setList(xml.getList());
			}
			case ".ser" -> {
				try
				{
					budgetDataTable.getDataBox().setList((LinkedList<Transaction>)SufuPersistence.<Transaction>deserialize(path));
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
	
	private String addExtension(String path)
	{
		if (!path.contains("."))
			return path += getFileFilter().getDescription();
		return path;
	}
	
	private void loadCSV(String path)
	{
		LinkedList<Transaction> l = null;
		try
		{
			l = SufuPersistence.<Transaction>loadFromCSV(path, new Transaction(), ";", 5);
		}
		catch (NullPointerException | IOException | IndexOutOfBoundsException e)
		{
			DialogHelper.exceptionDialog(budgetDataTable, e);
			return;
		}
		budgetDataTable.getDataBox().setList(l);
	}
}
