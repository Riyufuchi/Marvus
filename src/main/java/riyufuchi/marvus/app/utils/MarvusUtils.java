package riyufuchi.marvus.app.utils;

import java.io.IOException;
import java.util.List;

import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;

public class MarvusUtils
{
	private MarvusUtils() {}
	
	public static String[] loadCategoryList()
	{
		String path =  MarvusConfig.workFolder + "category.txt";
		String[] categoryList = { "Custom" }; 
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
			DialogHelper.exceptionDialog(null, e);
			generateCategoryList(path);
		}
		return categoryList;
	}

	private static void generateCategoryList(String path)
	{
		try
		{
			SufuFileHelper.checkFile(path);
			SufuPersistence.saveToCSV(path, new String[]{"Custom"});
			DialogHelper.informationDialog(null, "Generated default " + path, "Category list fixer info");
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(null, e);
		}
	}
}
