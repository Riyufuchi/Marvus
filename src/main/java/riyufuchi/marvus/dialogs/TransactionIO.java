package riyufuchi.marvus.dialogs;

import java.io.IOException;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.marvusLib.records.FileInput;
import riyufuchi.sufuLib.gui.SufuFileChooserGeneric;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 27.03.2023
 * @version 18.06.2024
 */
public class TransactionIO extends SufuFileChooserGeneric<MarvusDataWindow>
{
	public TransactionIO(MarvusDataWindow budgetDataTable, String filePath)
	{
		super(budgetDataTable, filePath);
	}

	@Override
	protected void onSave(String path)
	{
		path = addExtension(path);
		try
		{
			if (MarvusIO.saveData(parentFrame, path, parentFrame.getController().getDatabase(), false))
				SufuDialogHelper.informationDialog(parentFrame, "Succesfuly saved to:\n" + path, "Save progress");
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
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
		fi.setDataTo(parentFrame.getController());
		parentFrame.getController().displayData();
	}
	
	private String addExtension(String path)
	{
		if (!path.contains("."))
			return path += getFileFilter().getDescription();
		return path;
	}
}
