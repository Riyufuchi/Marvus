package riyufuchi.marvus.marvusLib.dataStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.interfaces.MarvusCollection;

/**
 * This class sort data into categories. Data starts from x = 0
 * 
 * @author Riyufuchi
 * @version 2.4 - 05.12.2023
 * @since 1.7 - 12.02.2024
 */
public class MarvusDataTable implements Serializable, MarvusCollection<Transaction>
{
	private ArrayList<LinkedList<Transaction>> months;
	private int x, size;
	
	public MarvusDataTable()
	{
		initialize();
		this.x = 0;
	}
	
	private void initialize()
	{
		this.months = new ArrayList<>();
		for (int i = 0; i < 12; i++)
			months.add(new LinkedList<>());
		this.size = 0;
	}
	
	// Java Collection
	
	@Override
	public boolean add(Transaction transaction)
	{
		if (transaction == null)
			return false;
		months.get(transaction.getDate().getMonthValue() - 1).add(transaction);
		size++;
		transaction.setID(size);
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends Transaction> c)
	{
		c.stream().forEach(item -> add(item));
		return true;
	}
	
	@Override
	public boolean remove(Object o)
	{
		if (o == null || !(o instanceof Transaction))
			return false;
		Transaction transaction = (Transaction)o;
		if(months.get(transaction.getDate().getMonthValue() - 1).remove(transaction))
		{
			size--;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		int preRemoveSize = size;
		c.stream().forEach(item -> remove(item));
		return size < preRemoveSize;
	}
	
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	@Override
	public void clear()
	{
		initialize();
	}
	
	@Override
	public int size()
	{
		return size;
	}
	
	@Override
	public boolean contains(Object o)
	{
		if (o == null)
			return false;
		return stream().anyMatch(pre -> pre.equals(o));
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		if (c == null || c.isEmpty())
			return false;
		int num = c.size();
		Iterator<?> it = c.iterator();
		for (Transaction t : this)
		{
			while (it.hasNext())
			{
				if (t.equals(it.next()))
				{
					try
					{
						it.remove();
					}
					catch (Exception e)
					{
						c.remove(t);
					}
					num--;
				}
			}
		}
		return num == 0;
	}
	
	@Override
	public Iterator<Transaction> iterator()
	{
		return toList().iterator();
	}
	
	// Marvus Collection
	
	@Override
	public LinkedList<Transaction> toList()
	{
		LinkedList<Transaction> data = new LinkedList<>();
		for (int i = 0; i < 12; i++)
		{
			data.addAll(months.get(i));
		}
		return data;
	}
	
	// Specific methods
	
	public void sortData(Comparator<Transaction> comp)
	{
		for(int i = 0; i < 12; i++)
		{
			Collections.sort(months.get(i), comp);
		}
	}
	
	public void rebuild()
	{
		Iterator<Transaction> it = iterator();
		clear();
		while (it.hasNext())
		{
			add(it.next());
		}
	}
	
	@Override
	public boolean set(Transaction transaction)
	{
		if (transaction == null)
			return false;
		x = transaction.getDate().getMonthValue() - 1;
		for (Transaction transactionOld : months.get(x))
			if (transactionOld.getID() == transaction.getID())
			{
				transactionOld = transaction;
				return true;
			}
		return false;
	}

	// Getters
	
	/**
	 * 
	 * @param index month number - 1
	 * @return
	 */
	public LinkedList<Transaction> getMonth(int index)
	{
		return months.get(index);
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}
}
