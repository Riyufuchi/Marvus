package riyufuchi.marvus.marvusLib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JFrame;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.sufuLib.gui.SufuDialogHelper;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 24.08.2023
 * <hr>
 * Utils for handling and formating dates
 * <hr>
 * @author Riyufuchi
 */
public class DateUtils
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(AppTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex]);
	//private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex]);
	
	public static Month showMonthChooser(JFrame frame)
	{
		return SufuDialogHelper.<Month>optionDialog(frame, "Choose month: ", "Month chooser", Month.values());
	}
	
	public static LocalDateTime toLocalDateTime(String date)
	{
		return LocalDateTime.ofInstant(formatDate(date).toInstant(), ZoneId.systemDefault());
	}
	
	public static String dateToString(String date)
	{
		return dateFormat.format(formatDate(date));
	}
	
	public static String nowDateString()
	{
		return dateFormat.format(new Date());
	}
	
	private static Date formatDate(String date)
	{
		try
		{
			 return dateFormat.parse(date);
		}
		catch (ParseException e)
		{
			return formatDate("10.10.1601");
		}
	}
}
