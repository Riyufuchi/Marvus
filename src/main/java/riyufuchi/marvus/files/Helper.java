package riyufuchi.marvus.files;

import java.io.IOException;
import java.time.LocalDate;

import riyufuchi.marvus.gui.windows.DataTableForm;
import riyufuchi.marvus.marvusData.Money;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 27.03.2023<br><br>
 * This class provides functions for some small features
 * @author Riyufuchi
 * @version 1.6
 * @since 1.0 
 */
public class Helper 
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
		String path = "data/backups/" + LocalDate.now() + "/";
		if(FileHelper.checkDirectory(path))
		{
			if(DialogHelper.yesNoDialog(dtf, "Are you really sure?\nThis action will overwrite existing backups.", "Backup creation") == 1)
				return;
		}
		else
		{
			DialogHelper.informationDialog(dtf, ("Created directory: " + path), "Backup directory created");
		}
		Persistance.<Money>saveToCSV(path, dtf.getDataBox().getList());
		new XML(path + ".xml", "MoneyExport", "Money").exportXML(dtf.getDataBox().getList());
		Persistance.<Money>serialize(path, dtf.getDataBox().getList());
		DialogHelper.informationDialog(dtf, "Backup successfuly created", "Task successful");
	}
}