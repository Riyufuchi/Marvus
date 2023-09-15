package riyufuchi.marvus.app.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;

public abstract class MarvusIO
{
	private MarvusIO() { }
	
	public static void quickSave(JFrame parentFrame, String path, List<Transaction> data)
	{
		if (saveData(parentFrame, path, data, true))
			SufuDialogHelper.informationDialog(parentFrame, "Succesfuly saved to:\n " + path, "Save message");
	}
	
	public static boolean saveData(JFrame parentFrame, String path, List<Transaction> data, boolean quicksava)
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
			default -> {
				SufuDialogHelper.errorDialog(parentFrame, "File is missing an extension or extension was not recognized\n" + "Extension: " + extension, "Extension not recognized");
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
