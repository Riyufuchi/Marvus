package riyufuchi.marvusLib.legacy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;

/**
 * Utils for handling and formating dates. This class have been moved to SufuLib<br>
 * 
 * Created On: 18.04.2023<br>
 * Last Edit: 12.09.2023
*
 * @author Riyufuchi
 */
@Deprecated
public class DateUtils
{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(MarvusTexts.DATE_FORMAT_OPTIONS[MarvusConfig.dateFormatIndex]);
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
