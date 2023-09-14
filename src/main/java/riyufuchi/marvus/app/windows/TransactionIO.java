package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import riyufuchi.marvus.app.utils.MarvusIO;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionXML;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;

/**
 * Created On: 27.03.2023<br>
 * Last Edit: 14.09.2023
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("deprecation")
public class TransactionIO extends SufuFileChooser
{
	private MarvusDataWindow budgetDataTable;
	
	public TransactionIO(MarvusDataWindow budgetDataTable, String filePath)
	{
		super(budgetDataTable, filePath);
		this.budgetDataTable = budgetDataTable;
	}

	@Override
	protected void onSave(String path)
	{
		path = addExtension(path);
		if (MarvusIO.saveData(budgetDataTable, path, budgetDataTable.getTable().getDataBox().getList(), false))
			SufuDialogHelper.informationDialog(budgetDataTable, "Succesfuly saved to:\n" + path, "Save progress");
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
				setData(xml.getList());
			}
			case ".ser" -> {
				try
				{
					budgetDataTable.getTable().getDataBox().setList((LinkedList<Transaction>)SufuPersistence.<Transaction>deserialize(path));
				}
				catch (NullPointerException | ClassNotFoundException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(budgetDataTable, e);
				}
			}
			default -> SufuDialogHelper.errorDialog(budgetDataTable, "File is missing an extension or extension was not recognized\n" + "Extension: " + extension, "Extension not recognized");
		}
		budgetDataTable.displayData();
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
			l = SufuPersistence.<Transaction>loadFromCSV(path, new Transaction(), ";", 6);
		}
		catch (NullPointerException | IOException | IndexOutOfBoundsException e)
		{
			SufuDialogHelper.exceptionDialog(budgetDataTable, e);
			return;
		}
		setData(l);
	}
	
	private void setData(LinkedList<Transaction> list)
	{
		budgetDataTable.getTable().getDataBox().setList(list);
		budgetDataTable.getTable().rebuild();
	}
}
