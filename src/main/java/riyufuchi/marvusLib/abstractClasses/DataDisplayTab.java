package riyufuchi.marvusLib.abstractClasses;

import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import riyufuchi.marvus.dialogs.transactions.EditDialog;
import riyufuchi.marvus.dialogs.transactions.RemoveDialog;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;
import riyufuchi.marvusLib.interfaces.MarvusTabbedFrame;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGridPane;

/**
 * @author Riyufuchi
 * @since 1.67
 * @version 15.11.2024
 */
public abstract class DataDisplayTab
{
	protected MarvusTabbedFrame targetWindow;
	protected MarvusDatabase dataSource;
	protected SufuGridPane masterPanel;
	private JPanel menuPanel;
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
		setPanel(targetWindow.getPane());
		this.ddt = parentTab;
	}
	
	public abstract void prepareUI();
	public abstract void displayData();
	public abstract void refresh();
	
	public void addMenuAndMenuItems(JComponent ... comp)
	{
		setPanel(targetWindow.getPane());
		menuPanel = SufuFactory.newFlowPane();
		SufuGridPane sgp = SufuFactory.newGridPane();
		masterPanel.add(menuPanel, masterPanel.getGBC(0, 0));
		masterPanel.add(sgp, masterPanel.getGBC(0, 1));
		masterPanel = sgp;
		for (JComponent c : comp)
			menuPanel.add(c);
	}
	
	public DataDisplayTab getSuperTab()
	{
		DataDisplayTab tab = ddt;
		while (tab.getParentTab() != null)
			tab = tab.getParentTab();
		return tab;
	}
	
	public DataDisplayTab getParentTab()
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
		setPanel(targetWindow.getPane());
	}
	
	public void setPanel(JPanel panel)
	{
		if (panel instanceof SufuGridPane)
		{
			this.masterPanel = (SufuGridPane) targetWindow.getPane();
		}
		else
		{
			masterPanel = new SufuGridPane();
			panel.add(masterPanel, targetWindow.getGBC(0, 0));
		}
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
