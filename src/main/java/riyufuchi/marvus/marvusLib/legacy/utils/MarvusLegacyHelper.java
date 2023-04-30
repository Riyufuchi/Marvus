package riyufuchi.marvus.marvusLib.legacy.utils;

import java.io.IOException;
import java.time.LocalDate;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusData.MoneySum;
import riyufuchi.marvus.marvusLib.files.XML;
import riyufuchi.marvus.marvusLib.legacy.gui.DataTableForm;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 01.05.2023<br><br>
 * This class provides functions for some small features
 * @author Riyufuchi
 * @version 1.7
 * @since 1.0 
 */
public class MarvusLegacyHelper 
{
	public static void backup(DataTableForm dtf) throws NullPointerException, IOException
	{
		if(dtf.getDataBox().getList().isEmpty())
		{
			DialogHelper.warningDialog(dtf, "No data to backup", "Backup error");
			return;
		}
		if(DialogHelper.yesNoDialog(dtf, "Are you sure?", "Data backup") == 1)
			return;
		String path = MarvusConfig.workFolder + "backups/" + LocalDate.now() + "/";
		if(FileHelper.checkDirectory(path))
		{
			if(DialogHelper.yesNoDialog(dtf, "Are you really sure?\nThis action will overwrite existing backups.", "Backup creation") == 1)
				return;
		}
		else
		{
			DialogHelper.informationDialog(dtf, ("Created directory: " + path), "Backup directory created");
		}
		path += "data";
		Persistance.<MoneySum>saveToCSV(path + ".csv", dtf.getDataBox().getList());
		new XML(path + ".xml", "MoneyExport", "Money").exportXML(dtf.getDataBox().getList());
		Persistance.<MoneySum>serialize(path + ".ser", dtf.getDataBox().getList());
		DialogHelper.informationDialog(dtf, "Backup successfuly created", "Task successful");
	}
}