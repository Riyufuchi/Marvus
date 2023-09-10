package riyufuchi.marvus.app.utils;

import java.io.IOException;
import java.util.List;

import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;

public class MarvusCategory
{
	public static String[] names, categories, values, categoryList;
	
	private MarvusCategory() {}
	
	public static int getIndexIn(String[] arr, String val)
	{
		int i = 0;
		for (String s : arr)
		{
			if (s.equals(val))
				return i;
			i++;
		}
		return 0;
	}
	
	public static void init()
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
	
	private static String[] loadTransactionMacro()
	{
		String path =  MarvusConfig.workFolder + "transactionsMacro.txt";
		String[] categoryList = { "Custom;Other;0" };
		try
		{
			List<String> l = SufuPersistence.loadTextFile(path);
			categoryList = new String[l.size()];
			int i = 0;
			for (String s : l)
				categoryList[i++] = s;
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(null, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(null, path, new String[]{"Custom;Other;0"});
		}
		return categoryList;
	}
	
	private static void loadCategoryList()
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
			SufuDialogHelper.exceptionDialog(null, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(null, path, new String[]{ "Other" });
		}
	}
}
