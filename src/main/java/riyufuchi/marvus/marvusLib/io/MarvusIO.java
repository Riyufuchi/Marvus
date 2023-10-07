package riyufuchi.marvus.marvusLib.io;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataUtils.TransactionXML;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public abstract class MarvusIO
{
	private MarvusIO() { }
	
	public static void quickSave(JFrame parentFrame, String path, Collection<Transaction> data)
	{
		if (saveData(parentFrame, path, data, true))
			SufuDialogHelper.informationDialog(parentFrame, "Succesfuly saved to:\n " + path, "Save message");
	}
	
	public static boolean saveData(JFrame parentFrame, String path, Collection<Transaction> data, boolean quicksava)
	{
		String extension = path.substring(path.lastIndexOf('.'));
		if (!quicksava)
			if (!SufuFileHelper.overwriteProtection(parentFrame, path))
				return false;
		switch(extension)
		{
			case ".csv" -> {
				try
				{
					SufuPersistence.<Transaction>saveToCSV(path, data);
					return true;
				}
				catch (NullPointerException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(parentFrame, e);
					return false;
				}
			}
			case ".ser" -> {
				try
				{
					SufuPersistence.<Transaction>serialize(path, data);
					return true;
				}
				catch (NullPointerException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(parentFrame, e);
					return false;
				}
			}
			case ".xml" -> {
				new TransactionXML(path).exportXML(data);
				return true;
			}
			default -> {
				SufuDialogHelper.errorDialog(parentFrame, "File is missing an extension or was not recognized\n" + "Extension: " + extension, "Extension not recognized");
				return false;
			}
		}
	}
	
	public static LinkedList<Transaction> loadData(JFrame parentFrame, String path)
	{
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> { return loadCSV(parentFrame, path); }
			case ".ser" -> {
				try
				{
					return ((LinkedList<Transaction>)SufuPersistence.<Transaction>deserialize(path));
				}
				catch (NullPointerException | ClassNotFoundException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(parentFrame, e);
				}
			}
			case ".xml" -> { return new TransactionXML(path).importXML(); }
			default -> SufuDialogHelper.errorDialog(parentFrame, "File is missing an extension or extension was not recognized\n" + "Extension: " + extension, "Extension not recognized");
		}
		return new LinkedList<>();
	}
	
	private static LinkedList<Transaction> loadCSV(JFrame parentFrame, String path)
	{
		LinkedList<Transaction> l = new LinkedList<>();
		try
		{
			l = SufuPersistence.<Transaction>loadFromCSV(path, new Transaction(), ";", 6);
		}
		catch (NullPointerException | IOException | IndexOutOfBoundsException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
		}
		return l;
	}
}
