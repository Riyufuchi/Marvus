package riyufuchi.marvus.legacy.utils;

import java.io.IOException;
import java.time.LocalDate;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.legacy.data.MoneySum;
import riyufuchi.marvus.legacy.gui.DataTableForm;
import riyufuchi.sufuLib.files.SufuFileHelper;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;

/**
 * This class provides functions for some small features<br><br>
 * 
 * @author Riyufuchi
 * @since 1.0 - 20.04.2022
 * @version 2.1 - 18.11.2024
 */
public class MarvusLegacyHelper 
{
	@SuppressWarnings("deprecation")
	public static void backup(DataTableForm dtf) throws NullPointerException, IOException
	{
		if(dtf.getDataBox().getList().isEmpty())
		{
			SufuDialogHelper.warningDialog(dtf, "No data to backup", "Backup error");
			return;
		}
		if(SufuDialogHelper.yesNoDialog(dtf, "Are you sure?", "Data backup") == 1)
			return;
		String path = MarvusConfig.workFolder + "backups/" + LocalDate.now() + "/";
		if(SufuFileHelper.checkDirectory(path))
		{
			if(SufuDialogHelper.yesNoDialog(dtf, "Are you really sure?\nThis action will overwrite existing backups.", "Backup creation") == 1)
				return;
		}
		else
		{
			SufuDialogHelper.informationDialog(dtf, ("Created directory: " + path), "Backup directory created");
		}
		path += "data";
		SufuPersistence.<MoneySum>saveToCSV(path + ".csv", dtf.getDataBox().getList());
		new XML(path + ".xml", "MoneyExport", "Money").exportXML(dtf.getDataBox().getList());
		SufuPersistence.<MoneySum>serialize(path + ".ser", dtf.getDataBox().getList());
		SufuDialogHelper.informationDialog(dtf, "Backup successfuly created", "Task successful");
	}
}