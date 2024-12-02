package riyufuchi.marvusLib.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusDefaultTableValues;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.general.SufuGeneralUtils;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 1.3 - 12.10.2023
 * @since 02.12.2024
 */
public class MaruvsDatabaseUtils implements Serializable
{
	private String[] categoryEnum, namesEnum;
	private LinkedList<String> categoryCSV, namesCSV;
	private ArrayList<TransactionMacro> transactionMacros;
	private JFrame parentFrame;
	
	public MaruvsDatabaseUtils()
	{
		this(null);
	}
	
	public MaruvsDatabaseUtils(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
		this.categoryCSV = null;
		initialize();
	}
	
	public void initialize()
	{
		initTransactionMacro(loadTransactionMacro());
		categoryEnum = loadCategoryList();
		namesEnum = loadEntityNames();
	}
	
	public void initTransactionMacro(String[] inputData)
	{
		String[] split = null;
		this.transactionMacros = new ArrayList<>(inputData.length);
		for (String line : inputData)
		{
			split = line.split(";", 3);
			transactionMacros.add(new TransactionMacro(split[0], split[1], split[2]));
		}
	}
	
	// LOAD FUNCTIONS
	
	private LinkedList<String> loadEntityNamesFile()
	{
		try
		{
			return SufuPersistence.loadTextFile(MarvusConfig.NAME_FILE_PATH);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				SufuFileHelper.generateFile(parentFrame, MarvusConfig.NAME_FILE_PATH, MarvusDefaultTableValues.ENTITY_NAME_ENUM);
		}
		return null;
	}
	
	private String[] loadEntityNames()
	{
		namesCSV = loadEntityNamesFile();
		if (namesCSV == null)
			if ((namesCSV = loadEntityNamesFile()) == null)
				return MarvusDefaultTableValues.ENTITY_NAME_ENUM;
		return namesCSV.toArray(new String[categoryCSV.size()]);
	}
	
	private LinkedList<String> loadTransactionMacroFile()
	{
		try
		{
			return SufuPersistence.loadTextFile(MarvusConfig.TRANSACTION_MACRO_FILE_PATH);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				SufuFileHelper.generateFile(parentFrame, MarvusConfig.TRANSACTION_MACRO_FILE_PATH, MarvusDefaultTableValues.MACROS);
		}
		return null;
	}
	
	/**-
	 * @return raw transaction macro in csv form
	 */
	private String[] loadTransactionMacro()
	{
		LinkedList<String> transactionMacroCSV = loadTransactionMacroFile();
		if (transactionMacroCSV == null)
			if ((transactionMacroCSV = loadTransactionMacroFile()) == null)
				return MarvusDefaultTableValues.MACROS;
		return transactionMacroCSV.toArray(new String[transactionMacroCSV.size()]);
	}
	
	private LinkedList<String> loadCategoryEnumFile()
	{
		try
		{
			return SufuPersistence.loadTextFile(MarvusConfig.CATEGORY_FILE_PATH);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				SufuFileHelper.generateFile(parentFrame, MarvusConfig.CATEGORY_FILE_PATH, categoryEnum);
		}
		return null;
	}
	
	private String[] loadCategoryList()
	{
		categoryCSV = loadCategoryEnumFile();
		if (categoryCSV == null)
			if ((categoryCSV = loadCategoryEnumFile()) == null)
				return MarvusDefaultTableValues.CATEGORY_ENUM;
		return categoryCSV.toArray(new String[categoryCSV.size()]);
	}
	
	// COLLECTION METHODS
	
	public void addEntityName(String name)
	{
		if (name == null)
			return;
		namesEnum = SufuGeneralUtils.addToArray(namesEnum, name);
	}
	
	public void addCategory(String[] newCategories)
	{
		if (newCategories == null)
			return;
		categoryEnum = SufuGeneralUtils.addToArray(categoryEnum, newCategories);
	}
	
	public void addMacro(TransactionMacro transactionMacro)
	{
		if (transactionMacro == null || getMacroIndex(transactionMacro.name()) != -1)
			return;
		transactionMacros.add(transactionMacro);
	}
	
	public void removeMacro(TransactionMacro transactionMacro)
	{
		if (transactionMacro == null)
			return;
		if (transactionMacros.remove(transactionMacro))
			if (transactionMacros.isEmpty())
				initTransactionMacro(MarvusDefaultTableValues.MACROS);
	}
	
	// SETTERS
	
	public void setParentframe(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}
	
	public boolean setMacro(String oldName, TransactionMacro transactionMacro)
	{
		final int INDEX = getMacroIndex(oldName);
		transactionMacros.set(INDEX, transactionMacro);
		return true;
	}
	
	public void setCategory(String oldCategory, String newCategory)
	{
		if (newCategory == null || oldCategory == null)
			return;
		for (int i = 0; i < categoryEnum.length; i++)
		{
			if (categoryEnum[i].equals(oldCategory))
			{
				categoryEnum[i] = newCategory;
				return;
			}
			
		}
	}
	
	// GETTERS
	
	public ArrayList<TransactionMacro> getTransactionMacros()
	{
		return transactionMacros;
	}
	
	public TransactionMacro getMacro(int index)
	{
		if (index >= 0 && index < transactionMacros.size())
			return transactionMacros.get(index);
		else
			return getMacro(0);
	}
	
	public String[] getEntityNamesEnum()
	{
		return namesEnum;
	}
	
	/**
	 * @return the category enum from enum table
	 */
	public String[] getCategoryEnum()
	{
		return categoryEnum;
	}

	/**
	 * 
	 * @param name
	 * @return index of given macro or -1 if macro for given name is not defined
	 */
	public int getMacroIndex(String name)
	{
		int i = 0;
		for (TransactionMacro s : transactionMacros)
		{
			if (s.name().equals(name))
				return i;
			i++;
		}
		return -1;
	}
}
