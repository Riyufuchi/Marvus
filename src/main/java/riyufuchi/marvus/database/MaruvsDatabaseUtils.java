package riyufuchi.marvus.database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusDefaultTableValues;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.general.SufuGeneralUtils;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @version 1.3 - 12.10.2023
 * @version 12.12.2024
 */
public class MaruvsDatabaseUtils implements Serializable
{
	private String[] categoryEnum;
	private ArrayList<String> namesEnum;
	private JFrame parentFrame;
	
	public MaruvsDatabaseUtils()
	{
		this(null);
	}
	
	public MaruvsDatabaseUtils(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
		initialize();
	}
	
	private void initialize()
	{
		this.categoryEnum = loadCategoryList();
		this.namesEnum = new ArrayList<>(Arrays.asList(loadEntityNames()));
	}
	
	// LOAD FUNCTIONS
	
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
	
	private String[] loadEntityNames()
	{
		LinkedList<String> namesCSV = loadTableFromFile(MarvusConfig.NAME_FILE_PATH, MarvusDefaultTableValues.ENTITY_NAME_ENUM);
		if (namesCSV == null)
			if ((namesCSV = loadTableFromFile(MarvusConfig.NAME_FILE_PATH, MarvusDefaultTableValues.ENTITY_NAME_ENUM)) == null)
				return MarvusDefaultTableValues.ENTITY_NAME_ENUM;
		return namesCSV.toArray(new String[namesCSV.size()]);
	}
	
	private String[] loadCategoryList()
	{
		LinkedList<String> categoryCSV = loadTableFromFile(MarvusConfig.CATEGORY_FILE_PATH, MarvusDefaultTableValues.CATEGORY_ENUM);
		if (categoryCSV == null)
			if ((categoryCSV = loadTableFromFile(MarvusConfig.CATEGORY_FILE_PATH, MarvusDefaultTableValues.CATEGORY_ENUM)) == null)
				return MarvusDefaultTableValues.CATEGORY_ENUM;
		return categoryCSV.toArray(new String[categoryCSV.size()]);
	}
	
	// COLLECTION METHODS
	
	public void addEntityName(String name)
	{
		if (name == null || namesEnum.contains(name))
			return;
		namesEnum.add(name);
	}
	
	public void removeEntityName(int index)
	{
		if (index >= 0 && index < namesEnum.size())
			namesEnum.remove(index);
	}
	
	public void sortEntityNames()
	{
		namesEnum.sort((m1, m2) -> m1.compareTo(m2));
	}
	
	public void addCategory(String[] newCategories)
	{
		if (newCategories == null)
			return;
		categoryEnum = SufuGeneralUtils.addToArray(categoryEnum, newCategories);
	}
	
	// SETTERS
	
	public void setParentframe(JFrame parentFrame)
	{
		this.parentFrame = parentFrame;
	}
	
	public void setEntityName(int index, String name)
	{
		if (name == null || index < 0 || index > namesEnum.size())
			return;
		namesEnum.set(index, name);
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
	
	public String[] getEntityNamesEnum()
	{
		return namesEnum.toArray(new String[0]);
	}
	
	/**
	 * @return the category enum from enum table
	 */
	public String[] getCategoryEnum()
	{
		return categoryEnum;
	}
}
