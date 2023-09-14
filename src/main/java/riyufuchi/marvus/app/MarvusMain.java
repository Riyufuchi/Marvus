package riyufuchi.marvus.app;

import java.io.IOException;
import java.util.LinkedList;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.legacyApp.gui.DataTableForm;
import riyufuchi.marvus.marvusLib.dataStorage.TransactionDataTable;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.time.SufuTimer;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 13.09.2023
 *
 * @author Riyufuchi
 */
public class MarvusMain
{
	private static SufuWindow app;
	private static SufuTimer timer;
	
	public static void main(String[] args)
	{
		loadSettings();
		timer = new SufuTimer(500);
		CustomizeUI.setUI(MarvusConfig.appTheme);
		for(String arg : args)
		{
			switch (arg)
			{
				case "--legacy" -> app = new DataTableForm(800, 600);
			}
			//if(arg.equals("--legacy"))
				//app = new DataTableForm(800, 600);
		}
		if(app == null)
			app = new MarvusDataWindow(1280, 720);
		app.toFront();
	}
	
	public static void loadSettings()
	{
		LinkedList<String> data = null;
		try
		{
			data = SufuPersistence.loadTextFile(MarvusConfig.SETTINGS_FILE_PATH);
			MarvusConfig.appTheme = AppThemeUI.valueOf(data.getFirst()); // Throws exception when inputed incorrect data
		}
		catch (IllegalArgumentException | NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(app, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(app, MarvusConfig.SETTINGS_FILE_PATH, new String[] { MarvusConfig.appTheme.toString(),
						AppTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex]});
		}
	}
	
	public static void fullScreen()
	{
		if(timer.intervalOk())
		{
			TransactionDataTable table = ((MarvusDataWindow)app).getTable();
			SufuWindow w2;
			if(app.isResizable())
			{
				w2 = new MarvusDataWindow();
			}
			else
			{
				w2 = new MarvusDataWindow(800, 600);
				//w2 = null;
			}
			app.dispose();
			app = w2;
			((MarvusDataWindow)app).setTable(table);
			((MarvusDataWindow)app).displayData();
		}
	}
}
