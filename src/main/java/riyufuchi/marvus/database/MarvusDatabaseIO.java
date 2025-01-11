package riyufuchi.marvus.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusDefaultTableValues;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.database.SufuAutoIncrement;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.files.SufuFileHelper;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;
import riyufuchi.sufuLib.interfaces.SufuDatabaseInterface;

public class MarvusDatabaseIO
{
	private JFrame parentFrame;
	
	public MarvusDatabaseIO(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}
	
	public <K extends Serializable, E extends Serializable> boolean saveTableToFile(String path, SufuDatabaseInterface<K, E> table)
	{
		try
		{
			SufuPersistence.serializeStructure(path, (SufuTableDB<K, E>)table);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return false;
		}
		return true;
	}
	
	public SufuTableDB<Integer, String> loadCategoryTable()
	{
		try
		{
			return SufuPersistence.<SufuTableDB<Integer, String>>deserialize(MarvusConfig.CATEGORY_TABLE_PATH).getFirst();
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e1)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e1);
			return new SufuTableDB<>(restoreTable(MarvusConfig.CATEGORY_FILE_PATH, MarvusDefaultTableValues.CATEGORY_ENUM), new SufuAutoIncrement(0));
		}
	}
	
	// Entities
	
	public SufuTableDB<Integer, String> loadEntityTable()
	{
		try
		{
			return SufuPersistence.<SufuTableDB<Integer, String>>deserialize(MarvusConfig.ENTITY_TABLE_PATH).getFirst();
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e1)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e1);
			return new SufuTableDB<>(restoreTable(MarvusConfig.ENTITY_FILE_PATH, MarvusDefaultTableValues.ENTITY_NAME_ENUM), new SufuAutoIncrement(0));
		}
	}
	
	private LinkedList<String> restoreTable(String path, String[] defaultValues)
	{
		LinkedList<String> namesCSV = loadTableFromFile(path, defaultValues);
		if (namesCSV == null)
		{
			if ((namesCSV = loadTableFromFile(path, defaultValues)) == null)
			{
				namesCSV = new LinkedList<>();
				for (String s : defaultValues)
					namesCSV.add(s);
				return namesCSV;
			}
		}
		return namesCSV;
	}
	
	// Macros
	
	public SufuTableDB<String, TransactionMacro> loadTransactionMacroTable()
	{
		try
		{
			return SufuPersistence.<SufuTableDB<String, TransactionMacro>>deserialize(MarvusConfig.TRANSACTION_MACRO_TABLE_PATH).getFirst();
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e1)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e1);
			return new SufuTableDB<>(restoreTransactionMacro(), e -> { return e.name(); });
		}
	}
	
	private ArrayList<TransactionMacro> restoreTransactionMacro()
	{
		String[] inputData = loadTransactionMacro();
		String[] split = null;
		ArrayList<TransactionMacro> transactionMacros = new ArrayList<>(inputData.length);
		for (String line : inputData)
		{
			split = line.split(";", 3);
			transactionMacros.add(new TransactionMacro(split[0], split[1], split[2]));
		}
		return transactionMacros;
	}
	
	/**
	 * @return raw transaction macro in csv form
	 */
	private String[] loadTransactionMacro()
	{
		LinkedList<String> transactionMacroCSV = loadTableFromFile(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, MarvusDefaultTableValues.MACROS);
		if (transactionMacroCSV == null)
			if ((transactionMacroCSV = loadTableFromFile(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, MarvusDefaultTableValues.MACROS)) == null)
				return MarvusDefaultTableValues.MACROS;
		return transactionMacroCSV.toArray(new String[transactionMacroCSV.size()]);
	}
	
	private LinkedList<String> loadTableFromFile(String path, String[] defaultData)
	{
		try
		{
			return SufuPersistence.loadTextFile(path);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				SufuFileHelper.generateFile(parentFrame, path, defaultData);
		}
		return null;
	}
}
