package riyufuchi.marvus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.legacyApp.gui.DataTableForm;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.enums.AppTheme;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;
import riyufuchi.sufuLib.utils.time.SufuTimer;

/**
 * @author Riyufuchi
 * @version 09.10.2023
 * @since 1.0 - 20.04.2022
 */
public class MarvusMainThread
{
	private static SufuWindow app;
	private static SufuTimer timer;
	
	public static void main(String[] args)
	{
		System.setProperty("sun.java2d.opengl", "true");
		loadSettings();
		timer = new SufuTimer(500);
		CustomizeUI.setUI(MarvusConfig.appTheme);
		for(String arg : args)
		{
			switch (arg)
			{
				case "--legacy" -> {
					if (MarvusConfig.fullscreen)
						app = new DataTableForm();
					else
						app = new DataTableForm(MarvusConfig.width, MarvusConfig.height);
					}
			}
		}
		if(app == null)
			if (MarvusConfig.fullscreen)
				app = new MarvusDataWindow();
			else
				app = new MarvusDataWindow(MarvusConfig.width, MarvusConfig.height);
		app.toFront();
	}
	
	public static void loadSettings()
	{
		LinkedList<String> data = null;
		try
		{
			data = SufuPersistence.loadTextFile(MarvusConfig.SETTINGS_FILE_PATH);
			if (data.getFirst().equals(AppTexts.WINDOW_SIZE[0]))
			{
				MarvusConfig.fullscreen = true;
			}
			else
			{
				MarvusConfig.width = Integer.valueOf(data.getFirst().substring(0, data.getFirst().indexOf("x")));
				MarvusConfig.height = Integer.valueOf(data.getFirst().substring(data.getFirst().indexOf("x") + 1));
			}
			MarvusConfig.appTheme = AppTheme.valueOf(data.get(1)); // Throws exception when inputed incorrect data
			SufuDateUtils.setDateFormat(new SimpleDateFormat(AppTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex = Integer.valueOf(data.get(2))]));
			MarvusConfig.currentWorkFile = new File(data.get(3));
			MarvusConfig.showQuitDialog = Boolean.valueOf(data.get(4));
		}
		catch (Exception e)
		{
			SufuDialogHelper.exceptionDialog(app, e);
			if (e instanceof IOException)
				MarvusUtils.generateFile(app, MarvusConfig.SETTINGS_FILE_PATH, 
						"800x600",
						MarvusConfig.appTheme.toString(),
						AppTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex],
						"None",
						"true");
		}
	}
	
	public static void fullScreen()
	{
		if(timer.isIntervalOk() && app instanceof MarvusDataWindow)
		{
			MarvusDataWindow w2;
			if(app.isResizable())
			{
				w2 = new MarvusDataWindow();
			}
			else
			{
				w2 = new MarvusDataWindow(800, 600);
			}
			w2.setDataDisplayMode(((MarvusDataWindow)app).getDataDisplayMode());
			app.dispose();
			app = w2;
			((MarvusDataWindow)app).displayData();
		}
	}
}
