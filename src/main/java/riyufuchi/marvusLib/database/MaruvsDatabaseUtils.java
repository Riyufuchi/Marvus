package riyufuchi.marvusLib.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.swing.JFrame;

import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.general.SufuGeneralUtils;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 1.3 - 12.10.2023
 * @since 31.08.2024
 */
public class MaruvsDatabaseUtils implements Serializable
{
	private String[] names, categories, values, categoryEnum;
	private List<String> transactionMacroCSV, categoryCSV;
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
		int i = 0;
		String[] split = null;
		String[] data = loadTransactionMacro();
		// Initialize values
		this.names = new String[data.length];
		this.categories = new String[data.length];
		this.values = new String[data.length];
		for (String line : data)
		{
			split = line.split(";", 3);
			names[i] = split[0];
			categories[i] = split[1];
			values[i] = split[2];
			i++;
		}
		loadCategoryList();
	}
	
	// LOAD FUNCTIONS
	
	private List<String> loadTransactionMacroFile()
	{
		try
		{
			return SufuPersistence.loadTextFile(MarvusConfig.TRANSACTION_MACRO_FILE_PATH);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(parentFrame, MarvusConfig.TRANSACTION_MACRO_FILE_PATH, categoryEnum);
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
		categoryEnum = new String[transactionMacroCSV.size()];
		return transactionMacroCSV.toArray(categoryEnum);
	}
	
	private List<String> loadCategoryEnumFile()
	{
		try
		{
			return SufuPersistence.loadTextFile(MarvusConfig.CATEGORY_FILE_PATH);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(parentFrame, MarvusConfig.CATEGORY_FILE_PATH, categoryEnum);
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
	
	// SETTERS
	
	public void setParentframe(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}
	
	public void setMacro(TransactionMacro transactionMacro)
	{
		if (transactionMacro == null)
			return;
		names = SufuGeneralUtils.addToArray(names, transactionMacro.name());
		categories = SufuGeneralUtils.addToArray(categories, transactionMacro.category());
		values = SufuGeneralUtils.addToArray(values, transactionMacro.value());
		transactionMacroCSV.add(transactionMacro.toCSV());
	}
	public void setCategory(String[] newCategories)
	{
		if (newCategories == null)
			return;
		categoryEnum = SufuGeneralUtils.addToArray(categoryEnum, newCategories);
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
