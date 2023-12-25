package riyufuchi.marvus.app.controller;

import javax.swing.JFrame;

import riyufuchi.marvus.app.utils.AppTexts;
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
}
