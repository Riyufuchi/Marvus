package riyufuchi.marvus.app.windows;

import java.util.LinkedList;

import riyufuchi.marvus.app.utils.MarvusIO;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * Created On: 27.03.2023<br>
 * Last Edit: 14.09.2023
 * 
 * @author Riyufuchi
 */
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
		setData(MarvusIO.loadData(budgetDataTable, path));
		budgetDataTable.displayData();
	}
	
	private String addExtension(String path)
	{
		if (!path.contains("."))
			return path += getFileFilter().getDescription();
		return path;
	}
	
	private void setData(LinkedList<Transaction> list)
	{
		budgetDataTable.getTable().getDataBox().setList(list);
		budgetDataTable.getTable().rebuild();
	}
}
