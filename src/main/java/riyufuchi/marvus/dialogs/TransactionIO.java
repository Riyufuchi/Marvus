package riyufuchi.marvus.dialogs;

import java.io.File;
import java.io.IOException;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.io.MarvusIO;
import riyufuchi.sufuLib.gui.SufuFileChooserGeneric;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 27.03.2023
 * @version 09.09.2024
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
	protected boolean onLoad(String path)
	{
		File newFile = new File(addExtension(path));
		if (!parentFrame.containsTabNamed(newFile.getName()))
		{
			MarvusConfig.currentWorkFile = newFile;
			TabController newCon = new TabController(parentFrame);
			parentFrame.newTab(newCon);
			if (!newCon.quickOpenFile())
			{
				parentFrame.removeLastTab();
				return false;
			}
			return true;
		}
		return false;
	}
}
