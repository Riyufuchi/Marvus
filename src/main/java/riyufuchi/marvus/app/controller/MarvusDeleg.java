package riyufuchi.marvus.app.controller;

import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JFrame;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * This is controller for main window, it contains delegations for events.
 * 
 * @author Riyufuchi
 * @version 18.12.2023
 * @since 18.12.2023
 */
public class MarvusDeleg
{
	private MarvusDeleg()
	{
	}
	
	public static void aboutMarvus(JFrame marvusWindow)
	{
		SufuDialogHelper.informationDialog(marvusWindow, AppTexts.ABOUT_MARVUS, "About");
	}
	
	public static void backupData(MarvusDataFrame mdw) throws NullPointerException, IOException
	{
		if(mdw.getController().getDatabase().isEmpty())
		{
			SufuDialogHelper.warningDialog(mdw.getSelf(), "No data to backup", "Backup error");
			return;
		}
		if(SufuDialogHelper.yesNoDialog(mdw.getSelf(), "Are you sure?", "Data backup") == 1)
			return;
		String path = MarvusConfig.workFolder + "backups/" + LocalDate.now() + "/";
		if(SufuFileHelper.checkDirectory(path))
		{
			if(SufuDialogHelper.yesNoDialog(mdw.getSelf(), "Are you really sure?\nThis action will overwrite existing backups.", "Backup creation") == 1)
				return;
		}
		else
		{
			SufuDialogHelper.informationDialog(mdw.getSelf(), ("Created directory: " + path), "Backup directory created");
		}
		path += "data";
		SufuPersistence.<Transaction>saveToCSV(path + ".csv", mdw.getController().getDatabase());
		SufuDialogHelper.informationDialog(mdw.getSelf(), "Backup successfuly created", "Task successful");
	}
}
