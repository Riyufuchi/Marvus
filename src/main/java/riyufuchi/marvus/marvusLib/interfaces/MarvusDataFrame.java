package riyufuchi.marvus.marvusLib.interfaces;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.sufuLib.interfaces.SufuWindowCommon;

/**
 * This interface is for windows, that will display data using tools provided by <i>MarvusLib</i>
 * 
 * @author Riyufuchi
 */
public interface MarvusDataFrame extends SufuWindowCommon
{
	void updateDataDisplayMode(DataDisplayMode dataDisplayMode);
	void refresh();
	
	/**
	 * This method return it self, it is here to ensure, that this interface is implemented on JFrame as MarvusLib is Swing based library.
	 * 
	 * @return reference to itself 
	 */
	JFrame getSelf();
	
}
