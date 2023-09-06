package riyufuchi.marvus.marvusLib.dataDisplay;

import riyufuchi.sufuLib.gui.SufuWindow;

/**
 * 
 * @author riyufuchi
 * @version 1.0
 * @since 0.1.67
 */
public abstract class DataDisplayMode
{
	protected SufuWindow targetWindow;
	protected CategoryYearTable dataSource;
	
	public DataDisplayMode(SufuWindow targetWindow, CategoryYearTable source)
	{
		this.targetWindow = targetWindow;
		this.dataSource = source;
	}
	
	public abstract void displayData();
	public abstract void refresh();
}
