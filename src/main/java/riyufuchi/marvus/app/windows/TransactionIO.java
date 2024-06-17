package riyufuchi.marvus.app.windows;

import java.io.IOException;

import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvus.marvusLib.io.MarvusIO;
import riyufuchi.marvus.marvusLib.records.FileInput;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 27.03.2023
 * @version 17.06.2024
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
		FileInput fi;
		try
		{
			fi = MarvusIO.inputFile(addExtension(path));
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		fi.setDataTo(budgetDataTable.getController());
		budgetDataTable.displayData();
	}
	
	private String addExtension(String path)
	{
		if (!path.contains("."))
			return path += getFileFilter().getDescription();
		return path;
	}
}
