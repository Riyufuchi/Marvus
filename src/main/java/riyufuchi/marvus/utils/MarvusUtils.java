package riyufuchi.marvus.utils;

import java.io.IOException;
import java.util.Collection;

import javax.swing.JFrame;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public class MarvusUtils
{
	private MarvusUtils() {}
	
	
	
	public static void generateFile(JFrame frame, String path, String ... fileContent)
	{
		if (path == null)
			path = "";
		if (fileContent == null)
			fileContent = new String[] {"No content was specified!"};
		try
		{
			SufuFileHelper.checkFile(path);
			SufuPersistence.saveToCSV(path, fileContent);
			SufuDialogHelper.informationDialog(frame, "Generated default " + path, "File generator info");
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(frame, e);
		}
	}
	
	public static void fixCategory(JFrame frame, Collection<Transaction> data)
	{
		if(SufuDialogHelper.yesNoDialog(frame, "Set category automatically?", "Category fixing") == 0)
			data.stream().forEach(transaction -> transaction.setCategory(MarvusDatabase.utils.getCategories()[getCategoryID(transaction.getName())]));
	}
	
	private static int getCategoryID(String name)
	{
		int i = 0;
		for (String s : MarvusDatabase.utils.getNames())
			if (name.equals(s))
				return i;
			else
				i++;
		return 0;
	}
}
