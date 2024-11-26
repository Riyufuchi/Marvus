package riyufuchi.marvus.utils;

import java.util.Collection;
import java.util.function.Consumer;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * This is controller for main window, it contains delegations for events.
 * 
 * @author Riyufuchi
 * @since 18.12.2023
 * @version 18.11.2024
 */
public class MarvusUtils
{
	private MarvusUtils() {}
	
	public static void fixCategory(JFrame frame, Collection<Transaction> data)
	{
		if (SufuDialogHelper.booleanDialog(frame, "Set category automatically?", "Category fixing"))
			data.stream().forEach(transaction -> transaction.setCategory(MarvusDatabase.utils.getCategories()[MarvusDatabase.utils.getCategoryID(transaction.getName())]));
	}
	
	public static void aboutMarvus(JFrame marvusWindow)
	{
		SufuDialogHelper.informationDialog(marvusWindow, MarvusTexts.ABOUT_MARVUS, "About");
	}
	
	public static void consumeFunction(TabController controller, Consumer<Iterable<Transaction>> consumer)
	{
		if (controller.isOperationUnexucatable())
			return;
		consumer.accept(controller.getDatabase());
	}
}
