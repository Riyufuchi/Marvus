package riyufuchi.marvus.marvusLib.legacy.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import riyufuchi.marvus.marvusData.MoneySum;
import riyufuchi.marvus.marvusLib.files.XML;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.FileSelector;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 27.03.2023<br>
 * Last Edit: 17.04.2023
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public class FileIO extends FileSelector
{
	private DataTableForm dtf;
	
	public FileIO(DataTableForm dtf, String path)
	{
		super(path);
		this.dtf = dtf;
	}

	@Override
	protected void onSave(String path)
	{
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				try
				{
					Persistance.<MoneySum>saveToCSV(path, dtf.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					DialogHelper.exceptionDialog(dtf, e);
				}
			}
			case ".xml" -> {
				XML xml = new XML(path, "MoneyExport", "Money");
				xml.exportXML(dtf.getDataBox().getList());
			}
			case ".ser" -> {
				try
				{
					Persistance.serialize(path, dtf.getDataBox().getList());
				}
				catch (NullPointerException | IOException e)
				{
					DialogHelper.exceptionDialog(dtf, e);
				}
			}
		}
	}
	
	@Override
	protected void onLoad(String path)
	{
		if(!path.contains("."))
		{
			DialogHelper.errorDialog(dtf, "File is missing an extension", "Extension not recognized");
			return;
		}
		String extension = path.substring(path.lastIndexOf('.'));
		switch(extension)
		{
			case ".csv" -> {
				List<String> list = null;
				LinkedList<MoneySum> l = new LinkedList<MoneySum>();
				String[] split = null;
				try
				{
					list = Persistance.loadFromCSV(path);
					Iterator<String> it = list.iterator();
					while(it.hasNext())
					{
						split = it.next().split(";");
						l.add(new MoneySum(split[0], split[1]));
					}
				}
				catch (NullPointerException | IOException | IndexOutOfBoundsException e)
				{
					DialogHelper.exceptionDialog(dtf, e);
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
					dtf.getDataBox().setList((LinkedList<MoneySum>)Persistance.<MoneySum>deserialize(path));
				}
				catch (NullPointerException | ClassNotFoundException | IOException e)
				{
					DialogHelper.exceptionDialog(dtf, e);
				}
			}
		}
		dtf.refresh();
	}
}
