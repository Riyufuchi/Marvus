package riyufuchi.marvusLib.abstractClasses;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import riyufuchi.marvus.dialogs.EditDialog;
import riyufuchi.marvus.dialogs.RemoveDialog;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusDataFrame;

/**
 * @author Riyufuchi
 * @since 1.67
 * @version 1.7 - 19.07.2024
 */
public abstract class DataDisplayMode
{
	protected MarvusDataFrame targetWindow;
	protected MarvusDatabase dataSource;
	
	public DataDisplayMode(MarvusDataFrame targetWindow)
	{
		this.targetWindow = targetWindow;
		this.dataSource = targetWindow.getController().getDatabase();
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
	
	protected final String createBtnName(int month, int y)
	{
		return month + ";" + (y - 1);
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
