package riyufuchi.marvus.marvusLib.abstractClasses;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import riyufuchi.marvus.app.windows.dialogs.EditDialog;
import riyufuchi.marvus.app.windows.dialogs.RemoveDialog;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.dataBase.MarvusDatabase;
import riyufuchi.marvus.marvusLib.interfaces.MarvusDataFrame;

/**
 * 
 * @author riyufuchi
 * @version 1.5 - 07.10.2023
 * @since 1.67
 */
public abstract class DataDisplayMode
{
	protected MarvusDataFrame targetWindow;
	protected MarvusDatabase dataSource;
	
	/**
	 * 
	 * @param targetWindow
	 * @param dataSource
	 */
	public DataDisplayMode(MarvusDataFrame targetWindow, MarvusDatabase dataSource)
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
	
	public void setNewData(MarvusDatabase dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public void setTargetWindow(MarvusDataFrame targetWindow)
	{
		this.targetWindow = targetWindow;
	}
	
	// Getters
	
	public MarvusDatabase getDataSource()
	{
		return dataSource;
	}
}
