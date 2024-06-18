package riyufuchi.marvus.marvusLib.dataDisplay;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusLib.abstractClasses.DataDisplayMode;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.marvus.marvusLib.io.MarvusIO;
import riyufuchi.marvus.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;


/**
 * 
 * @author Riyufuchi
 * @since 11.02.2024
 * @version 12.02.2024
 */
public class MultiYearTable extends DataDisplayMode
{
	private LinkedList<YearOverview> dataSet;
	
	public MultiYearTable(MarvusDataFrame targetWindow)
	{
		super(targetWindow);
		this.dataSet = new LinkedList<>();
		dataSet.add(dataSource.getYearOverview(MarvusConfig.financialYear));
	}

	@Override
	public void displayData()
	{
		// Controls
		SufuGuiTools.addComponents(targetWindow, 0, 0,
				SufuFactory.newButton("Add table", evt -> addTable())
				);
		// Data display
		Iterator<YearOverview> it = dataSet.iterator();
		YearOverviewTable.buildTable(it.next(), targetWindow, 0, 1, true);
		int y = 6;
		while (it.hasNext())
		{
			YearOverviewTable.buildTable(it.next(), targetWindow, 0, y, false);
			y += 5;
		}
		
	}

	@Override
	public void refresh()
	{
		hardRefresh();
	}
	
	private void addTable()
	{
		String path = MarvusConfig.workFolder + "finance2023.csv";
		MarvusDatabase db = null;
		try
		{
			db = MarvusIO.inputFile(path).convertDataToDB();
		}
		catch (ClassNotFoundException | NullPointerException | ClassCastException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(targetWindow.getSelf(), e);
		}
		if (db == null)
		{
			SufuDialogHelper.errorDialog(targetWindow.getSelf(), "File not loaded", "Input error");
			return;
		}
		dataSet.add(db.getYearOverview(db.assumeYear()));
		//TODO: Sort data
		targetWindow.refresh();
	}
}
