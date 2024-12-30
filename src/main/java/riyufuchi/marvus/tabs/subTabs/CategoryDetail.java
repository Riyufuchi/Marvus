package riyufuchi.marvus.tabs.subTabs;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;

import riyufuchi.marvus.interfaces.MarvusTabbedFrame;
import riyufuchi.marvus.tabs.DataDisplayTab;
import riyufuchi.marvusLib.data.FinancialCategory;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.TransactionComparation;
import riyufuchi.marvusLib.dataUtils.TransactionComparation.CompareMethod;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

/**
 * @author riyufuchi
 * @since 19.08.2024
 * @version 09.11.2024
 */
public class CategoryDetail extends DataDisplayTab
{
	private FinancialCategory category;
	private BigDecimal total;
	private LinkedList<FinancialCategory> sortedCategory;
	private int y;
	
	public CategoryDetail(MarvusTabbedFrame targetWindow, FinancialCategory category, DataDisplayTab parentTab)
	{
		super(targetWindow);
		this.category = category;
		this.sortedCategory = new LinkedList<>();
		this.y = 1;
		this.total = new BigDecimal(0);
		setParentTab(parentTab);
	}

	@Override
	public void prepareUI()
	{
		prepData();
		//masterPanel.add(SufuFactory.newTextFieldHeader(category.getCategory()), targetWindow.getGBC(0, 0));
		masterPanel.add(SufuFactory.newButton(category.getCategory(), evt -> {
			TableDetail td = new TableDetail(targetWindow, sortedCategory.getFirst(), this);
			Iterator<FinancialCategory> it = sortedCategory.iterator();
			it.next();
			while (it.hasNext())
				td.addData(it.next());
			targetWindow.updateDataDisplayMode(td);
		}), targetWindow.getGBC(0, 0));
		masterPanel.add(SufuFactory.newTextFieldHeader("Category total"), targetWindow.getGBC(1, 0));
		y = 1;
		total = new BigDecimal(0);
	}

	@Override
	public void displayData()
	{
		for (FinancialCategory fc : sortedCategory)
		{
			masterPanel.add(SufuFactory.newButton(fc.getCategory(), String.valueOf(y),
				evt ->
					targetWindow.updateDataDisplayMode(new TableDetail(targetWindow, sortedCategory.get(Integer.valueOf(((JButton)evt.getSource()).getName()) - 1), this))),
				targetWindow.getGBC(0, y));
			masterPanel.add(SufuFactory.newTextFieldHeader(fc.getSum().toString()), targetWindow.getGBC(1, y));
			y++;
			total = total.add(fc.getSum());
		}
		masterPanel.add(SufuFactory.newTextFieldHeader("Total"), targetWindow.getGBC(0, y));
		masterPanel.add(SufuFactory.newTextFieldHeader(total.toString()), targetWindow.getGBC(1, y));
	}

	@Override
	public void refresh()
	{
		targetWindow.updateDataDisplayMode(getSuperTab());
	}
	
	private void prepData()
	{
		sortedCategory.clear();
		FinancialCategory holder = null;
		for (Transaction t : category)
		{
			holder = new FinancialCategory(t.getName(), t);
			for (FinancialCategory mc : sortedCategory)
			{
				if (mc.getCategory().equals(holder.getCategory()))
				{
					mc.add(t);
					holder = null;
					break;
				}
			}
			if(holder != null)
				sortedCategory.add(holder);
		}
		Collections.sort(sortedCategory, TransactionComparation.compareFC(CompareMethod.By_name));
	}
}
