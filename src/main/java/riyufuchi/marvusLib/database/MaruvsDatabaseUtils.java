package riyufuchi.marvusLib.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import riyufuchi.marvus.utils.MarvusConfig;
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
	private String[] names;
	private String[] categoryEnum, namesEnum;
	private LinkedList<String> transactionMacroCSV, categoryCSV, namesCSV;
	private ArrayList<TransactionMacro> transactionMacros;
	private JFrame parentFrame;
	
	public MaruvsDatabaseUtils()
	{
		this(null);
	}
	
	public MaruvsDatabaseUtils(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
		this.transactionMacroCSV = this.categoryCSV = null;
		initialize();
	}
	
	public void initialize()
	{
		initTransactionMacro(loadTransactionMacro());
		loadCategoryList();
		loadEntityNames();
	}
	
	public void initTransactionMacro(String[] inputData)
	{
		int i = 0;
		String[] split = null;
		this.names = new String[inputData.length];
		this.transactionMacros = new ArrayList<>(inputData.length);
		for (String line : inputData)
		{
			split = line.split(";", 3);
			names[i] = split[0];
			transactionMacros.add(new TransactionMacro(split[0], split[1], split[2]));
			i++;
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
				SufuFileHelper.generateFile(parentFrame, MarvusConfig.NAME_FILE_PATH, names);
		}
		return null;
	}
	
	private void loadEntityNames()
	{
		namesEnum = new String[]{ "Custom" };
		namesCSV = loadEntityNamesFile();
		if (namesCSV == null)
			if ((namesCSV = loadEntityNamesFile()) == null)
				return;
		namesEnum = namesCSV.toArray(new String[categoryCSV.size()]);
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
				SufuFileHelper.generateFile(parentFrame, MarvusConfig.TRANSACTION_MACRO_FILE_PATH, categoryEnum);
		}
		return null;
	}
	
	/**
	 * categoryEnum is used as memory placeholder instead of temporary array
	 * 
	 * @return raw transaction macro in csv form
	 */
	private String[] loadTransactionMacro()
	{
		categoryEnum = new String[]{ "Custom;Other;0" };
		transactionMacroCSV = loadTransactionMacroFile();
		if (transactionMacroCSV == null)
			if ((transactionMacroCSV = loadTransactionMacroFile()) == null)
				return categoryEnum;
		return transactionMacroCSV.toArray(categoryEnum);
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
	
	private void loadCategoryList()
	{
		categoryEnum = new String[]{ "Other" };
		categoryCSV = loadCategoryEnumFile();
		if (categoryCSV == null)
			if ((categoryCSV = loadCategoryEnumFile()) == null)
				return;
		categoryEnum = categoryCSV.toArray(new String[categoryCSV.size()]);
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
		if (transactionMacro == null)
			return;
		transactionMacroCSV.add(transactionMacro.toCSV());
		transactionMacros.add(transactionMacro);
	}
	
	public void removeMacro(TransactionMacro transactionMacro)
	{
		if (transactionMacro == null)
			return;
		if (transactionMacroCSV.remove(transactionMacro.toCSV()))
			if (transactionMacroCSV.isEmpty())
				initTransactionMacro(new String[]{ "Custom;Other;0" });
			else
				initTransactionMacro(transactionMacroCSV.toArray(new String[transactionMacroCSV.size() - 1]));
	}
	
	// SETTERS
	
	public void setParentframe(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}
	
	public boolean setMacro(String oldName, TransactionMacro transactionMacro)
	{
		final int INDEX = getMacroIndex(oldName);
		transactionMacroCSV.set(INDEX, transactionMacro.toCSV());
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
	
	public List<String> getTransactionMacroCSV()
	{
		return transactionMacroCSV;
	}
	
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
