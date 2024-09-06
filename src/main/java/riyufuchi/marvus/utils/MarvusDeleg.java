package riyufuchi.marvus.utils;


import java.util.function.Consumer;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.controller.MarvusController;
import riyufuchi.marvusLib.data.Transaction;
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
		SufuDialogHelper.informationDialog(marvusWindow, MarvusTexts.ABOUT_MARVUS, "About");
	}
	
	public static void consumeFunction(MarvusController controller, Consumer<Iterable<Transaction>> consumer)
	{
		if (controller.isOperationUnexucatable())
			return;
		consumer.accept(controller.getDatabase());
	}
}
