package riyufuchi.marvus.marvusLib.dataDisplay;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import riyufuchi.marvus.app.windows.dialogs.EditDialog;
import riyufuchi.marvus.app.windows.dialogs.RemoveDialog;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.sufuLib.gui.SufuWindow;

/**
 * 
 * @author riyufuchi
 * @version 1.1
 * @since 0.1.67
 */
public abstract class DataDisplayMode
{
	protected SufuWindow targetWindow;
	protected CategoryYearTable dataSource;
	
	public DataDisplayMode(SufuWindow targetWindow, CategoryYearTable dataSource)
	{
		this.targetWindow = targetWindow;
		this.dataSource = dataSource;
	}
	
	public abstract void displayData();
	public abstract void refresh();
	
	public void showExtednedInfo(Transaction t, MouseEvent mEvt)
	{
		if(SwingUtilities.isLeftMouseButton(mEvt))
		{
			new EditDialog(targetWindow, t).showDialog();
		}
		else if (SwingUtilities.isRightMouseButton(mEvt))
		{
			new RemoveDialog(targetWindow, t).showDialog();
		}
	}
}