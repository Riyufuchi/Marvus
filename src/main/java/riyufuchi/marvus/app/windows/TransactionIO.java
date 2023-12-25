package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.LinkedList;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvus.marvusLib.io.MarvusIO;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 25.12.2023
 * @since 27.03.2023
 */
public class TransactionIO extends SufuFileChooser
{
	private MarvusDataFrame budgetDataTable;
	
	public TransactionIO(MarvusDataFrame budgetDataTable, String filePath)
	{
		super(budgetDataTable.getSelf(), filePath);
		this.budgetDataTable = budgetDataTable;
	}

	@Override
	protected void onSave(String path)
	{
		path = addExtension(path);
		try
		{
			if (MarvusIO.saveData(budgetDataTable.getSelf(), path, budgetDataTable.getController().getDatabase(), false))
				SufuDialogHelper.informationDialog(budgetDataTable.getSelf(), "Succesfuly saved to:\n" + path, "Save progress");
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(budgetDataTable.getSelf(), e);
		}
	}

	@Override
	protected void onLoad(String path)
	{
		path = addExtension(path);
		LinkedList<?> list = null;
		try
		{
			list = MarvusIO.loadData(path);
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(budgetDataTable.getSelf(), e);
			return;
		}
		setData(path, list);
		budgetDataTable.displayData();
	}
	
	private String addExtension(String path)
	{
		if (!path.contains("."))
			return path += getFileFilter().getDescription();
		return path;
	}
	
	@SuppressWarnings("unchecked")
	private void setData(String path, LinkedList<?> list)
	{
		switch (MarvusIO.getExtension(path))
		{
			case ".dat" -> budgetDataTable.getController().setDatabase((MarvusDatabase)list.getFirst());
			default -> budgetDataTable.getController().getDatabase().addAll((LinkedList<Transaction>)list);
		}
	}
}
