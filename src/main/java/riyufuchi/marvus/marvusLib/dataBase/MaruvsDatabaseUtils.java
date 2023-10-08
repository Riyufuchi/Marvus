package riyufuchi.marvus.marvusLib.dataBase;

import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.marvusLib.records.TransactionMacro;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 08.10.2023
 * @since 07.10.2023
 */
public class MaruvsDatabaseUtils
{
	private String[] names, categories, values, categoryList;
	private JFrame parentFrame;
	
	public MaruvsDatabaseUtils()
	{
		initialize();
		this.parentFrame = null;
	}
	
	public MaruvsDatabaseUtils(JFrame parentFrame)
	{
		initialize();
		this.parentFrame = parentFrame;
	}
	
	public void initialize()
	{
		String[] data = loadTransactionMacro();
		names = new String[data.length];
		categories = new String[data.length];
		values = new String[data.length];
		int i = 0;
		String[] split = null;
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
	
	private String[] loadTransactionMacro()
	{
		String path =  MarvusConfig.workFolder + "transactionsMacro.txt";
		String[] categoryList = { "Custom;Other;0" };
		List<String> l = null;
		try
		{
			l = SufuPersistence.loadTextFile(path);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(parentFrame, path, categoryList);
		}
		categoryList = new String[l.size()];
		int i = 0;
		for (String s : l)
			categoryList[i++] = s;
		return categoryList;
	}
	
	private void loadCategoryList()
	{
		String path = MarvusConfig.workFolder + "category.txt";
		try
		{
			List<String> list = SufuPersistence.loadTextFile(path);
			categoryList = new String[list.size()];
			categoryList = list.toArray(categoryList);
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(parentFrame, path, new String[]{ "Other" });
		}
	}
	
	// SETTERS
	
	public void setParentframe(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}
	
	// GETTERS
	
	public TransactionMacro getMacro(int index)
	{
		if (index > 0 && index < names.length)
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
	public String[] getCategoryList()
	{
		return categoryList;
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
