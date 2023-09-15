package riyufuchi.marvus.marvusLib.interfaces;

import javax.swing.JFrame;

import riyufuchi.marvus.marvusLib.dataDisplay.DataDisplayMode;
import riyufuchi.sufuLib.interfaces.SufuWindowCommon;

public interface MarvusDataFrame extends SufuWindowCommon
{
	void updateDataDisplayMode(DataDisplayMode dataDisplayMode);
	
	/**
	 * This method return it self, it is here to ensure, that this interface is implemented on JFrame as MarvusLib is Swing based library.
	 * 
	 * @return this
	 */
	JFrame getSelf();
}
