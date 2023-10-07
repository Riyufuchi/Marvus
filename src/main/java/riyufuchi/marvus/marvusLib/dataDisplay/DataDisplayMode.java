package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import riyufuchi.marvus.app.windows.dialogs.EditDialog;
import riyufuchi.marvus.app.windows.dialogs.RemoveDialog;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataStorage.MarvusDataTable;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;

/**
 * 
 * @author riyufuchi
 * @version 1.4
 * @since 1.67
 */
public abstract class DataDisplayMode
{
	protected MarvusDataFrame targetWindow;
	protected MarvusDataTable dataSource;
	
	/**
	 * 
	 * @param targetWindow
	 * @param dataSource
	 */
	public DataDisplayMode(MarvusDataFrame targetWindow, MarvusDataTable dataSource)
	{
		this.targetWindow = targetWindow;
		this.dataSource = dataSource;
	}
	
	public abstract void displayData();
	public abstract void refresh();
	
	/**
	 * Hard refresh is used when refresh() haven't been implemented yet
	 */
	public void hardRefresh()
	{
		targetWindow.getPane().removeAll();
		displayData();
	}
	
	public void showExtednedInfo(Transaction t, MouseEvent mEvt)
	{
		if(SwingUtilities.isLeftMouseButton(mEvt))
		{
			new EditDialog(targetWindow.getSelf(), t).showDialog();
		}
		else if (SwingUtilities.isRightMouseButton(mEvt))
		{
			new RemoveDialog(targetWindow.getSelf(), t).showDialog();
		}
	}
	
	// Setters
	
	public void setNewData(MarvusDataTable dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public void setTargetWindow(MarvusDataFrame targetWindow)
	{
		this.targetWindow = targetWindow;
	}
	
	// Getters
	
	public MarvusDataTable getDataSource()
	{
		return dataSource;
	}
}
