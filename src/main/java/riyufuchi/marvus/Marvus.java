package riyufuchi.marvus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.legacy.gui.DataTableForm;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.enums.AppTheme;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.time.SufuDateUtils;
import riyufuchi.sufuLib.utils.time.SufuTimer;

/**
 * @author Riyufuchi
 * @since 20.04.2022
 * @version 18.06.2024
 */
public class Marvus
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
		{
			if (MarvusConfig.fullscreen)
				app = new MarvusDataWindow();
			else
				app = new MarvusDataWindow(MarvusConfig.width, MarvusConfig.height);
			if (MarvusConfig.autoMaximize)
				app.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		app.toFront();
	}
	
	private static void createConfigFile()
	{
		MarvusUtils.generateFile(app, MarvusConfig.SETTINGS_FILE_PATH,
				(MarvusConfig.width + "x" + MarvusConfig.height),
				MarvusConfig.appTheme.toString(),
				Integer.toString(MarvusConfig.dateFormatIndex),
				MarvusConfig.currentWorkFile.toString(),
				Boolean.toString(MarvusConfig.showQuitDialog),
				Boolean.toString(MarvusConfig.autoLoadData),
				Boolean.toString(MarvusConfig.autoMaximize));
	}
	
	public static void loadSettings()
	{
		LinkedList<String> data = null;
		try
		{
			data = SufuPersistence.loadTextFile(MarvusConfig.SETTINGS_FILE_PATH);
			if (data.getFirst().equals(MarvusTexts.WINDOW_SIZE[0]))
			{
				MarvusConfig.fullscreen = true;
			}
			else
			{
				MarvusConfig.width = Integer.valueOf(data.getFirst().substring(0, data.getFirst().indexOf("x")));
				MarvusConfig.height = Integer.valueOf(data.getFirst().substring(data.getFirst().indexOf("x") + 1));
			}
			MarvusConfig.appTheme = AppTheme.valueOf(data.get(1)); // Throws exception when inputed incorrect data
			SufuDateUtils.setDateFormat(new SimpleDateFormat(MarvusTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex = Integer.valueOf(data.get(2))]));
			MarvusConfig.currentWorkFile = new File(data.get(3));
			MarvusConfig.defaultWorkFile = MarvusConfig.currentWorkFile;
			MarvusConfig.showQuitDialog = Boolean.valueOf(data.get(4));
			MarvusConfig.autoLoadData = Boolean.valueOf(data.get(5));
			MarvusConfig.autoMaximize = Boolean.valueOf(data.get(6));
		}
		catch (Exception e)
		{
			SufuDialogHelper.exceptionDialog(app, e);
			if ((e instanceof IOException) || (e instanceof IndexOutOfBoundsException))
				createConfigFile();
			else if (SufuDialogHelper.yesNoDialog(app, "Generate config file?", "New config file") == 0)
				createConfigFile();
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
			w2.setDataDisplayMode(((MarvusDataWindow)app).getCurrentTab());
			app.dispose();
			app = w2;
			((MarvusDataWindow)app).displayData();
		}
	}
}
