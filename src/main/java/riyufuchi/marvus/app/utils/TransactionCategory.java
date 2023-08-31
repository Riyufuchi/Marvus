package riyufuchi.marvus.app.utils;

import java.io.IOException;
import java.util.List;

import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;

public class TransactionCategory
{
	public static String[] names, categories, values, categoryList;
	
	private TransactionCategory()
	{
		
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
			generateTransactionMacro(path);
		}
		return categoryList;
	}
	
	private static void generateTransactionMacro(String path)
	{
		try
		{
			SufuFileHelper.checkFile(path);
			SufuPersistence.saveToCSV(path, new String[]{"Custom;Other;0"});
			SufuDialogHelper.informationDialog(null, "Generated default " + path, "Transaction macro fixer info");
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(null, e);
		}
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
			generateCategoryList(path);
		}
	}
	
	private static void generateCategoryList(String path)
	{
		try
		{
			SufuFileHelper.checkFile(path);
			SufuPersistence.saveToCSV(path, new String[]{ "Other" });
			SufuDialogHelper.informationDialog(null, "Generated default " + path, "Category list fixer info");
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(null, e);
		}
	}
}
