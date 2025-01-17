package riyufuchi.marvus.interfaces;

import riyufuchi.marvus.tabs.utils.DataDisplayTab;

/**
 * This interface is for windows/controllers, that will display data using tools provided by <i>MarvusLib</i>
 * 
 * @author Riyufuchi
 */
public interface MarvusTabbedFrame extends MarvusFrame
{
	void updateDataDisplayMode(DataDisplayTab dataDisplayMode);
	/**
	 * Refresh displayed data
	 */
	void refresh();
	/**
	 * Displays data, use only when changing displayMode or displaying entirely new data
	 */
	void displayData();
	/**
	 * This setter sets data display mode when changing fullscreen and windowed mode
	 * 
	 * @param ddm
	 */
	void setDataDisplayMode(DataDisplayTab ddm);
	DataDisplayTab getCurrentTab();
	DataDisplayTab getPreviousTab();
}
