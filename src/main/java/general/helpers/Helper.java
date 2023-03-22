package general.helpers;

import java.io.File;

import general.persistance.FilesIO;
import general.persistance.XML;
import gui.windows.DataTableForm;
import sufuSoft.sufuLib.gui.DialogHelper;

/**
 * Created On: 20.04.2022
 * Last Edit: 22.03.2023
 * 
 * @author Riyufuchi
 * @version 1.5
 * @since 1.0 
 */

public class Helper 
{
	public static void backup(DataTableForm dtf)
	{
		if(dtf.getDataBox().getList().isEmpty())
		{
			DialogHelper.warningDialog(dtf, "No data to backup", "Backup error");
			return;
		}
		if(DialogHelper.yesNoDialog(dtf, "Are you sure?\nThis action will overwrite existing backups.", "Backup creation") == 1)
			return;
		String path = "data/backups/backup";
		File folder = new File(path.substring(0, path.lastIndexOf("/")));
		folder.mkdirs();
		FilesIO.saveToCSV(path + ".csv", dtf.getDataBox().getList());
		new XML(path + ".xml", "MoneyExport", "Money").exportXML(dtf.getDataBox().getList());
		FilesIO.writeBinary(path + ".dat", dtf.getDataBox().getList());
	}
}