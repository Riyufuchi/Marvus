package riyufuchi.marvusLib.database;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import riyufuchi.marvusLib.interfaces.IDatabase;
import riyufuchi.marvusLib.records.Row;

/**
 * 
 * @param <E>
 * @author riyufuchi
 * @since 10.12.2024
 * @version 18.12.2024
 */
public class MarvusDatabaseTable<E extends Serializable> implements IDatabase<E>, Serializable
{
	private static final long serialVersionUID = 1L;
	private Dictionary<Integer, E> data;
	private int id;
	
	public MarvusDatabaseTable()
	{
		this.data = new Hashtable<>();
		this.id = 0;
	}
	
	public MarvusDatabaseTable(Iterable<E> collection)
	{
		this();
		for (E e : collection)
			add(e);
	}
	
	@Override
	public boolean add(E e)
	{
		id++;
		return null == data.put(id, e);
	}
	
	@Override
	public boolean remove(int ID)
	{
		return null != data.remove(ID);
	}

	@Override
	public boolean set(int ID, E e)
	{
		return null != data.put(ID, e);
	}
	
	@Override
	public LinkedList<Row<E>> getRows()
	{
		List<E> list = Collections.list(data.elements());
		List<Integer> list_ids = Collections.list(data.keys());
		LinkedList<Row<E>> finalList = new LinkedList<>();
		int i = 0;
		for (E e : list)
		{
			finalList.add(new Row<E>(list_ids.get(i), e));
			i++;
		}
		return finalList;
	}
	
	public LinkedList<Row<E>> getRows(Comparator<Row<E>> comparator)
	{
		LinkedList<Row<E>> finalList = getRows();
		Collections.sort(finalList, comparator);
		return finalList;
	}

	@Override
	public Optional<E> getByID(int ID)
	{
		return Optional.ofNullable(data.get(ID));
	}

	@Override
	public List<E> getData()
	{
		return Collections.list(data.elements());
	}
	
	public int getCount()
	{
		return data.size();
	}
}
