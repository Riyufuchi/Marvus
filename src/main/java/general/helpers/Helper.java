package general.helpers;

import java.io.File;
import java.util.Objects;

import general.persistance.FilesIO;
import general.persistance.XML;
import gui.utils.DialogHelper;
import gui.windows.DataTableForm;

/**
 * Copyright Header
 * 
 * Created On: 20.04.2022
 * Last Edit: 06.09.2022
 * 
 * @author Riyufuchi
 * @version 1.5
 * @since 1.0 
 */

public class Helper 
{
	public static boolean overwriteProtection(String path)
	{
		File file = new File(path);
		if(file.exists())
			if (DialogHelper.yesNoDialog(null, path + " already exist.\nDo you want to overwrite this file?", "File already exists") == 0)
				return true;
			else
				return false;
		return true;
	}
	
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
	
	public static String checkDoubleFormat(String text) throws NullPointerException, IllegalArgumentException
	{
		Objects.requireNonNull(text);
		if(text.isBlank() || !text.matches(".*\\d.*"))
			throw new IllegalArgumentException();
		if(text.contains(","))
			text = text.replace(",", ".");
		char[] number = new char[text.length()];
		boolean dot = false;
		int index = 0;
		for(int i = 0; i < text.length(); i++)
		{
			if(Character.isDigit(text.charAt(i)))
			{
				number[index] = text.charAt(i);
				index++;
			}
			else if (text.charAt(i) == '.')
			{
				if(dot)
					continue;
				dot = true;
				number[index] = text.charAt(i);
				index++;
			}
		}
		return String.valueOf(number).trim();
	}
}