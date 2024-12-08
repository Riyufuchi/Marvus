package riyufuchi.marvusLib.interfaces;

import riyufuchi.marvusLib.records.MarvusDataSummary;

/**
 * This interface should make easier to make application work with SQL database
 *
 * @author riyufuchi
 * @since 09.09.2024
 * @version 08.12.2024
 */
public interface MarvusQuerriable
{
	boolean updateAtribbute(String attr, String oldValue, String newValue);
	boolean updateAtribbute(String whereAttr, String whereValue, String targetAttr, String oldValue, String newValue);
	MarvusDataSummary createDataSummary(int year);
}
