package riyufuchi.marvusLib.interfaces;

/**
 * This interface should make easier to make application work with SQL database
 *
 * @author riyufuchi
 * @since 09.09.2024
 * @version 09.09.2024
 */
public interface MarvusQuerriable
{
	boolean updateAtribbute(String attr, String oldValue, String newValue);
	boolean updateItemWhere(String whereAttr, String whereValue, String targetAttr, String newValue);
}