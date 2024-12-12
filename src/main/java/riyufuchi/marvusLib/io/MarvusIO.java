package riyufuchi.marvusLib.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.TransactionXML;
import riyufuchi.marvusLib.records.FileInput;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 12.02.2024
 * @version 12.08.2024
 */
public class MarvusIO
{
	private MarvusIO() { }
	
	public static void quickSave(JFrame parentFrame, String path, Collection<Transaction> data)
	{
		try
		{
			if (saveData(parentFrame, path, data, true))
				SufuDialogHelper.informationDialog(parentFrame, "Succesfuly saved to:\n " + path, "Save message");
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
		}
	}
	
	public static boolean saveData(JFrame parentFrame, String path, Collection<Transaction> data, boolean quicksave) throws FileNotFoundException, NullPointerException, IOException
	{
		String extension = getExtension(path);
		if (!quicksave) // Quick save true -> overwrite protection is skipped
			if (!SufuFileHelper.overwriteProtection(parentFrame, path))
				return false;
		switch(extension)
		{
			case ".csv" -> SufuPersistence.<Transaction>saveToCSV(path, data);
			case ".ser" -> SufuPersistence.<Transaction>serialize(path, data);
			case ".xml" -> new TransactionXML(path).exportXML(data);
			case MarvusConfig.MDB_EXT -> SufuPersistence.<MarvusDatabase>serializeStructure(path, (MarvusDatabase)data);
			default -> throw new IOException("File is missing an extension or extension was not recognized\n" + "Extension: " + extension);
		}
		return true;
	}
	
	public static FileInput inputFile(String path) throws FileNotFoundException, ClassNotFoundException, NullPointerException, ClassCastException, IOException
	{
		String extension = getExtension(path);
		switch(extension)
		{
			case ".csv" -> { return new FileInput(extension, SufuPersistence.<Transaction>loadFromCSV(path, new Transaction(), ";", 6)); }
			case ".ser" -> { return new FileInput(extension, ((LinkedList<Transaction>)SufuPersistence.<Transaction>deserialize(path))); }
			case ".xml" -> { return new FileInput(extension, new TransactionXML(path).importXML()); }
			case MarvusConfig.MDB_EXT -> { return new FileInput(extension, loadDatabase(path)); }
			default -> throw new IOException("File is missing an extension or extension was not recognized\n" + "Extension: " + extension);
		}
	}
	
	public static LinkedList<MarvusDatabase> loadDatabase(String path) throws FileNotFoundException, ClassNotFoundException, NullPointerException, ClassCastException, IOException
	{
		return SufuPersistence.<MarvusDatabase>deserialize(path);
	}
	
	public static String getExtension(String path)
	{
		if (path == null || !path.contains("."))
			return "";
		return path.substring(path.lastIndexOf('.'));
	}
}
