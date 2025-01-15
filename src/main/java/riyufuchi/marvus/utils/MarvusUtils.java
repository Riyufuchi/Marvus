package riyufuchi.marvus.utils;

import java.time.Month;
import java.util.function.Consumer;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.sufuLib.database.SufuAutoIncrement;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.gui.SufuFilePicker;
import riyufuchi.sufuLib.gui.utils.SufuDialogHelper;

/**
 * @author Riyufuchi
 * @since 18.12.2023
 * @version 16.01.2025
 */
public class MarvusUtils
{
	public static final SufuTableDB<Integer, Month> monthTable = new SufuTableDB<Integer, Month>(new SufuAutoIncrement(0));
	
	static {
		for (Month month : Month.values())
			monthTable.add(month);
	}
	
	private MarvusUtils() {}
	
	public static void createNewTab(MarvusDataWindow mdw)
	{
		SufuFilePicker sfp =  new SufuFilePicker(mdw, MarvusConfig.defaultWorkFile.getAbsolutePath());
		sfp.setAcceptAllFileFilterUsed(false);
		sfp.setFileFilters(MarvusConfig.MDB, MarvusConfig.CSV);
		sfp.showFileCreator().ifPresent(file -> mdw.newTab(file.getName(), new TabController(mdw, file)));
	}
	
	public static void fixCategory(JFrame frame, MarvusDatabase database)
	{
		if (SufuDialogHelper.booleanDialog(frame, "Set category automatically?", "Category fixing"))
			database.getMacrosTable().getData()
				.forEach(macro -> database.updateAtribbute("Name", macro.name(), "Category", "", macro.category()));

	}
	
	public static void aboutMarvus(JFrame marvusWindow)
	{
		SufuDialogHelper.informationDialog(marvusWindow, MarvusTexts.ABOUT_MARVUS, "About");
	}
	
	public static void consumeFunction(TabController controller, Consumer<Iterable<Transaction>> consumer)
	{
		if (controller.isOperationUnexucatable())
			return;
		consumer.accept(controller.getDatabase().getTransactionsTable().getData());
	}
}
