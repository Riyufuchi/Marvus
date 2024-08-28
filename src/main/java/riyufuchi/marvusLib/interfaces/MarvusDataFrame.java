package riyufuchi.marvusLib.interfaces;

import javax.swing.JFrame;

import riyufuchi.marvusLib.abstractClasses.DataDisplayTab;
import riyufuchi.sufuLib.interfaces.SufuWindowCommon;

/**
 * This interface is for windows, that will display data using tools provided by <i>MarvusLib</i>
 * 
 * @author Riyufuchi
 */
public interface MarvusDataFrame extends SufuWindowCommon, MarvusControllable
{
	void updateDataDisplayMode(DataDisplayTab dataDisplayMode);
	void refresh();
	void displayData();
	DataDisplayTab getCurrentTab();
	DataDisplayTab getPreviousTab();
	
	/**
	 * This method return it self, it is here to ensure, that this interface is implemented on JFrame as MarvusLib is Swing based library.
	 * 
	 * @return reference to itself 
	 */
	JFrame getSelf();
}
