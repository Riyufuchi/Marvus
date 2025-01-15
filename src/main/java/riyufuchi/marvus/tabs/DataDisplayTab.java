package riyufuchi.marvus.tabs;

import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import riyufuchi.marvus.dialogs.transactions.EditTransactionDialog;
import riyufuchi.marvus.dialogs.transactions.RemoveTransactionDialog;
import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;
import riyufuchi.sufuLib.gui.utils.SufuFactory;
import riyufuchi.sufuLib.gui.utils.SufuGridPane;

/**
 * @author Riyufuchi
 * @since 1.67
 * @version 15.01.2025
 */
public abstract class DataDisplayTab
{
	protected MarvusTabbedFrame targetWindow;
	protected MarvusDatabaseController database;
	protected SufuGridPane masterPanel, contentPanel;
	private JPanel menuPanel;
	private DataDisplayTab ddt;
	
	public DataDisplayTab(MarvusTabbedFrame targetWindow)
	{
		this(targetWindow, targetWindow.getController().getDatabase(), null);
	}
	
	public DataDisplayTab(MarvusTabbedFrame targetWindow, MarvusDatabaseController mdb)
	{
		this(targetWindow, mdb, null);
	}
	
	public DataDisplayTab(MarvusTabbedFrame targetWindow, MarvusDatabaseController mdb, DataDisplayTab parentTab)
	{
		this.targetWindow = targetWindow;
		this.database = mdb;
		this.ddt = parentTab;
		this.masterPanel = new SufuGridPane();
		this.masterPanel.setDoubleBuffered(true);
	}
	
	public abstract void displayData();
	public abstract void refresh();
	
	/**
	 * Prepares static content such as menus. Deprecated after DataDisplayTab's update where tabs were given own panels
	 */
	@Deprecated
	public void prepareUI()
	{}
	
	/**
	 * This method creates menuPanel and adds it to master pane to y = 0 and also adds components to it
	 * 
	 * @param comps
	 */
	public void addMenuAndMenuItems(JComponent ... comps)
	{
		menuPanel = SufuFactory.newFlowPane();
		masterPanel.add(menuPanel, masterPanel.getGBC(0, 0));
		for (JComponent c : comps)
			menuPanel.add(c);
	}
	
	/**
	 * This method creates contentPanel and adds it to the masterPanel to y = 1
	 */
	public void addContentPanel()
	{
		contentPanel = new SufuGridPane();
		masterPanel.add(contentPanel, masterPanel.getGBC(0, 1));
	}
	
	public void addContentItem(JComponent comp, int x, int y)
	{
		contentPanel.add(comp, contentPanel.getGBC(x, y));
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
		prepareUI();
		displayData();
	}
	
	public void showExtednedInfo(Transaction t, MouseEvent mEvt)
	{
		if(SwingUtilities.isLeftMouseButton(mEvt))
		{
			new EditTransactionDialog(targetWindow, t, database).showDialog();
		}
		else if (SwingUtilities.isRightMouseButton(mEvt))
		{
			new RemoveTransactionDialog(targetWindow, t, database).showDialog();
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
	
	public void setNewData(MarvusDatabaseController dataSource)
	{
		this.database = dataSource;
	}
	
	public void setTargetWindow(MarvusTabbedFrame targetWindow)
	{
		this.targetWindow = targetWindow;
		targetWindow.setPane(masterPanel);
	}
	
	protected void setParentTab(DataDisplayTab parentTab)
	{
		this.ddt = parentTab;
	}
	
	// Getters
	
	public SufuGridPane getTabsPanel()
	{
		return masterPanel;
	}
	
	public MarvusDatabaseController getDataSource()
	{
		return database;
	}
}
