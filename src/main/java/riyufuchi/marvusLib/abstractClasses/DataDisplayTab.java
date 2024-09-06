package riyufuchi.marvusLib.abstractClasses;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import riyufuchi.marvus.dialogs.EditDialog;
import riyufuchi.marvus.dialogs.RemoveDialog;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;

/**
 * @author Riyufuchi
 * @since 1.67
 * @version 19.08.2024
 */
public abstract class DataDisplayTab
{
	protected MarvusTabbedFrame targetWindow;
	protected MarvusDatabase dataSource;
	protected JPanel masterPanel;
	private DataDisplayTab ddt;
	
	public DataDisplayTab(MarvusTabbedFrame targetWindow)
	{
		this(targetWindow, targetWindow.getController().getDatabase(), null);
	}
	
	public DataDisplayTab(MarvusTabbedFrame targetWindow, MarvusDatabase mdb)
	{
		this(targetWindow, mdb, null);
	}
	
	public DataDisplayTab(MarvusTabbedFrame targetWindow, MarvusDatabase mdb, DataDisplayTab parentTab)
	{
		this.targetWindow = targetWindow;
		this.dataSource = mdb;
		this.masterPanel = targetWindow.getPane();
		this.ddt = parentTab;
	}
	
	public abstract void prepareUI();
	public abstract void displayData();
	public abstract void refresh();
	
	public DataDisplayTab parentTab()
	{
		return ddt;
	}
	
	/**
	 * Hard refresh is used when refresh() haven't been implemented yet
	 */
	public void hardRefresh()
	{
		masterPanel.removeAll();
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
	
	/**
	 * @param panel targeted panel
	 * @param numOfHeaderItems
	 * 
	 * For this to work properly, header/menu components must be added before other items/components
	 */
	public void clearPanel(JPanel panel, int numOfHeaderItems)
	{
		int totalItems = panel.getComponentCount() - 1;
		for (int x = totalItems; x > numOfHeaderItems; x--)
			panel.remove(x);
	}
	
	@Deprecated
	public void clearPanel(JPanel panel, int numOfHeaderItems, int totalItems)
	{
		for (int x = totalItems; x > numOfHeaderItems; x--)
			panel.remove(x);
	}
	
	// Setters
	
	public void setNewData(MarvusDatabase dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public void setTargetWindow(MarvusTabbedFrame targetWindow)
	{
		this.targetWindow = targetWindow;
	}
	
	protected void setParentTab(DataDisplayTab parentTab)
	{
		this.ddt = parentTab;
	}
	
	// Getters
	
	public MarvusDatabase getDataSource()
	{
		return dataSource;
	}
}
