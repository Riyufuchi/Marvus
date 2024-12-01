package riyufuchi.marvusLib.database;

import java.io.IOException;
import java.io.Serializable;
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
 * @since 01.12.2024
 */
public class MaruvsDatabaseUtils implements Serializable
{
	private String[] names, categories, values, categoryEnum;
	private LinkedList<String> transactionMacroCSV, categoryCSV;
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
	}
	
	public void initTransactionMacro(String[] inputData)
	{
		int i = 0;
		String[] split = null;
		this.names = new String[inputData.length];
		this.categories = new String[inputData.length];
		this.values = new String[inputData.length];
		for (String line : inputData)
		{
			split = line.split(";", 3);
			names[i] = split[0];
			categories[i] = split[1];
			values[i] = split[2];
			i++;
		}
	}
	
	// LOAD FUNCTIONS
	
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
	
	public void addMacro(TransactionMacro transactionMacro)
	{
		if (transactionMacro == null)
			return;
		names = SufuGeneralUtils.addToArray(names, transactionMacro.name());
		categories = SufuGeneralUtils.addToArray(categories, transactionMacro.category());
		values = SufuGeneralUtils.addToArray(values, transactionMacro.value());
		transactionMacroCSV.add(transactionMacro.toCSV());
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
		final int INDEX = getMacroIndex(names, oldName);
		names[INDEX] = transactionMacro.name();
		categories[INDEX] = transactionMacro.category();
		values[INDEX] = transactionMacro.value();
		transactionMacroCSV.set(INDEX, transactionMacro.toCSV());
		return true;
	}
	
	public void addCategory(String[] newCategories)
	{
		if (newCategories == null)
			return;
		categoryEnum = SufuGeneralUtils.addToArray(categoryEnum, newCategories);
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
	
	public TransactionMacro getMacro(int index)
	{
		if (index >= 0 && index < names.length)
			return new TransactionMacro(names[index], categories[index], values[index]);
		else
			return getMacro(0);
	}
	
	/**
	 * @return the names
	 */
	public String[] getNames()
	{
		return names;
	}

	/**
	 * @return the categories
	 */
	public String[] getCategories()
	{
		return categories;
	}
	
	public String[] getValues()
	{
		return values;
	}
	
	/**
	 * @return the categoryList
	 */
	public String[] getCategoryEnum()
	{
		return categoryEnum;
	}
	
	public int getCategoryID(String name)
	{
		int i = 0;
		for (String s :names)
			if (name.equals(s))
				return i;
			else
				i++;
		return -1;
	}

	public int getMacroIndex(String[] arr, String name)
	{
		int i = 0;
		for (String s : arr)
		{
			if (s.equals(name))
				return i;
			i++;
		}
		return 0;
	}
}
