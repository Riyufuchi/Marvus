package riyufuchi.marvus.marvusLib.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionXML;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.marvus.marvusLib.records.FileInput;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 1.4 - 12.02.2024
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
		if (!quicksave) //quick save true -> overwrite protection is skipped
			if (!SufuFileHelper.overwriteProtection(parentFrame, path))
				return false;
		switch(extension)
		{
			case ".csv" -> SufuPersistence.<Transaction>saveToCSV(path, data);
			case ".ser" -> SufuPersistence.<Transaction>serialize(path, data);
			case ".xml" -> new TransactionXML(path).exportXML(data);
			case ".dat" -> SufuPersistence.<MarvusDatabase>serializeStructure(path, (MarvusDatabase)data);
			default -> throw new IOException("File is missing an extension or extension was not recognized\n" + "Extension: " + extension);
		}
		return true;
	}
	
	@Deprecated
	public static LinkedList<?> loadData(String path) throws FileNotFoundException, ClassNotFoundException, NullPointerException, ClassCastException, IOException
	{
		String extension = getExtension(path);
		switch(extension)
		{
			case ".csv" -> { return SufuPersistence.<Transaction>loadFromCSV(path, new Transaction(), ";", 6); }
			case ".ser" -> { return ((LinkedList<Transaction>)SufuPersistence.<Transaction>deserialize(path)); }
			case ".xml" -> { return new TransactionXML(path).importXML(); }
			case ".dat" -> { return loadDatabase(path); }
			default -> throw new IOException("File is missing an extension or extension was not recognized\n" + "Extension: " + extension);
		}
	}
	
	public static FileInput inputFile(String path) throws FileNotFoundException, ClassNotFoundException, NullPointerException, ClassCastException, IOException
	{
		String extension = getExtension(path);
		switch(extension)
		{
			case ".csv" -> { return new FileInput(extension, SufuPersistence.<Transaction>loadFromCSV(path, new Transaction(), ";", 6)); }
			case ".ser" -> { return new FileInput(extension, ((LinkedList<Transaction>)SufuPersistence.<Transaction>deserialize(path))); }
			case ".xml" -> { return new FileInput(extension, new TransactionXML(path).importXML()); }
			case ".dat" -> { return new FileInput(extension, loadDatabase(path)); }
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
