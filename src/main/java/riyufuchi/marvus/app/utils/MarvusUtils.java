package riyufuchi.marvus.app.utils;

import java.io.IOException;

import javax.swing.JFrame;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.app.windows.TransactionIO;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataStorage.DataBox;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public class MarvusUtils
{
	private MarvusUtils() {}
	
	public static TransactionIO createTransactionIO(MarvusDataWindow mdw)
	{
		TransactionIO fio = new TransactionIO(mdw, MarvusConfig.workFolder);
		fio.setFileFilters(MarvusConfig.SER, MarvusConfig.CSV);
		return fio;
	}
	
	public static void generateFile(JFrame frame, String path, String ... fileContent)
	{
		if (path == null)
			path = "";
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
	
	public static void fixCategory(JFrame frame,DataBox<Transaction> data)
	{
		if(SufuDialogHelper.yesNoDialog(frame, "Set category automatically?", "Category fixing") == 0)
			data.stream().forEach(transaction -> transaction.setCategory(MarvusCategory.categories[getCategoryID(transaction.getName())]));
	}
	
	private static int getCategoryID(String name)
	{
		int i = 0;
		for (String s : MarvusCategory.names)
			if (name.equals(s))
				return i;
			else
				i++;
		return 0;
	}
}
