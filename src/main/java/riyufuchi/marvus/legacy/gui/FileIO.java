package riyufuchi.marvus.legacy.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import riyufuchi.marvus.legacy.data.MoneySum;
import riyufuchi.marvus.legacy.utils.XML;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.gui.SufuFileChooser;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 27.03.2023
 * @version 09.09.2024
 */
@SuppressWarnings("deprecation")
public class FileIO extends SufuFileChooser
{
	private DataTableForm dtf;
	
	public FileIO(DataTableForm dtf, String path)
	{
		super(dtf, path);
		this.dtf = dtf;
	}

	@Override
	protected void onSave(String path)
	{
		if(!path.contains("."))
		{
			SufuDialogHelper.errorDialog(dtf, "File is missing an extension", "Extension not recognized");
			return;
		}
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				try
				{
					SufuPersistence.<MoneySum>saveToCSV(path, dtf.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(dtf, e);
				}
			}
			case ".xml" -> {
				XML xml = new XML(path, "MoneyExport", "Money");
				xml.exportXML(dtf.getDataBox().getList());
			}
			case ".ser" -> {
				try
				{
					SufuPersistence.serialize(path, dtf.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(dtf, e);
				}
			}
		}
	}
	
	@Override
	protected boolean onLoad(String path)
	{
		if(!path.contains("."))
		{
			SufuDialogHelper.errorDialog(dtf, "File is missing an extension", "Extension not recognized");
			return false;
		}
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				List<String> list = null;
				LinkedList<MoneySum> l = new LinkedList<>();
				String[] split = null;
				try
				{
					list = SufuPersistence.loadTextFile(path);
					Iterator<String> it = list.iterator();
					while(it.hasNext())
					{
						split = it.next().split(";");
						l.add(new MoneySum(split[0], split[1]));
					}
				}
				catch (NullPointerException | IOException | IndexOutOfBoundsException e)
				{
					SufuDialogHelper.exceptionDialog(dtf, e);
				}
				dtf.getDataBox().setList(l);
			}
			case ".xml" -> {
				XML xml = new XML(path, "MoneyExport", "Money");
				xml.parsujMoney();
				dtf.loadData(xml.getList());
			}
			case ".ser" -> {
				try
				{
					dtf.getDataBox().setList((LinkedList<MoneySum>)SufuPersistence.<MoneySum>deserialize(path));
				}
				catch (NullPointerException | ClassNotFoundException | IOException e)
				{
					SufuDialogHelper.exceptionDialog(dtf, e);
				}
			}
		}
		dtf.refresh();
		return true;
	}
}
