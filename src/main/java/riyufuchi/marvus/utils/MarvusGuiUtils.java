package riyufuchi.marvus.utils;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.dialogs.io.TransactionIO;
import riyufuchi.sufuLib.gui.SufuFilePicker;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

public final class MarvusGuiUtils
{
	private MarvusGuiUtils() {}
	
	public static void exitApp(JFrame mainFrame)
	{
		if (mainFrame == null)
			return;
		boolean result = true;
		if (MarvusConfig.showQuitDialog)
			result = SufuDialogHelper.booleanDialog(mainFrame, "Do you really want to exit the application?", "Exit confirmation");
		if (result)
			mainFrame.dispose();
	}
	
	public static TransactionIO createTransactionIO(MarvusDataWindow mdw)
	{
		TransactionIO fio = new TransactionIO(mdw, MarvusConfig.defaultWorkFile.getAbsolutePath());
		fio.setFileFilters(MarvusConfig.SER,  MarvusConfig.XML, MarvusConfig.MDB, MarvusConfig.CSV);
		return fio;
	}
	
	public static String pathSelector(JFrame parentFrame) throws NoSuchElementException
	{
		return new SufuFilePicker(parentFrame, MarvusConfig.defaultWorkFile.getAbsolutePath()).showFilePicker().orElseThrow().getPath();
	}
	
	public static File fileSelector(JFrame parentFrame) throws NoSuchElementException
	{
		return new SufuFilePicker(parentFrame, MarvusConfig.defaultWorkFile.getAbsolutePath()).showFilePicker().orElseThrow();
	}
	
	public static String encodeCords(int month, int y)
	{
		return month + ";" + y;
	}
	
	public static void editDateText(JButton button, LocalDateTime localDate)
	{
		button.setText((localDate.getDayOfMonth() + "." + localDate.getMonthValue() + "." + localDate.getYear()));
	}
	
	public static Point extractPointFromButtonName(ActionEvent e)
	{
		String point = ((JButton)e.getSource()).getName();
		return new Point(Integer.valueOf(point.substring(0, point.indexOf(';'))), Integer.valueOf(point.substring(point.indexOf(';') + 1, point.length())));
	}
}
