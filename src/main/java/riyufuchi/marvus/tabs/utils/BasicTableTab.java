package riyufuchi.marvus.tabs.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.TransactionTableModel;
import riyufuchi.sufuLib.gui.utils.SufuComponentTools;
import riyufuchi.sufuLib.gui.utils.SufuFactory;

/**
 * @author riyufuchi
 * @since 17.01.2025
 * @version 17.01.2025
 */
public abstract class BasicTableTab extends DataDisplayTab
{
	protected LinkedList<Transaction> currDataSet;
	private TransactionTableModel model;
	private JTable table;
	private TableRowSorter<TransactionTableModel> sorter;
	
	public BasicTableTab(MarvusTabbedFrame targetWindow)
	{
		super(targetWindow);
		this.currDataSet = new LinkedList<>();
		this.model = new TransactionTableModel(currDataSet);
		this.sorter = new TableRowSorter<>(model);
		this.table = SufuFactory.newTable(model);
		this.table.setRowSorter(sorter);
		this.table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int row = table.rowAtPoint(e.getPoint());
				if (row >= 0) // Ensure a valid cell/row was clicked
				{
					showExtednedInfo(model.getTransactionAt(table.convertRowIndexToModel(row)), e);
				}
			}
		});
		addMenuPanel();
		masterPanel.simulateBorderLayout();
		masterPanel.add(new JScrollPane(table), masterPanel.getGBC(0, 1));
	}
	
	// Overrides

	@Override
	public void displayData()
	{
		refresh();
	}

	@Override
	public void refresh()
	{
		model = new TransactionTableModel(currDataSet);
		sorter = new TableRowSorter<>(model);
		table.setModel(model);
		table.setRowSorter(sorter);
		SufuComponentTools.resizeColumnWidths(table);
	}
}
