package riyufuchi.marvusLib.database;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import riyufuchi.marvusLib.interfaces.ITableDB;
import riyufuchi.marvusLib.records.MarvusRow;

/**
 * If K (Key) would be integer use <code>riyufuchi.marvusLib.database.MarvusDataBaseTable</code> instead
 * 
 * @param <K>
 * @param <E> 
 * @author riyufuchi
 * @since 12.12.2024
 * @version 12.12.2024
 */
public class MarvusTableDB<K extends Serializable, E extends Serializable> implements ITableDB<K, E>, Serializable
{
	protected Dictionary<K, E> data;
	
	public MarvusTableDB()
	{
		this.data = new Hashtable<>();
	}
	
	public MarvusTableDB(Iterable<E> collection, Function<E, K> getID)
	{
		this();
		for (E e : collection)
			data.put(getID.apply(e), e);
	}
	
	@Override
	public boolean add(K ID, E e)
	{
		return null == data.put(ID, e);
	}
	
	@Override
	public boolean remove(K ID)
	{
		return null != data.remove(ID);
	}

	@Override
	public boolean set(K ID, E e)
	{
		return null != data.put(ID, e);
	}
	
	@Override
	public LinkedList<MarvusRow<K, E>> getRows()
	{
		List<E> list = Collections.list(data.elements());
		List<K> list_ids = Collections.list(data.keys());
		LinkedList<MarvusRow<K, E>> finalList = new LinkedList<>();
		int i = 0;
		for (E e : list)
		{
			finalList.add(new MarvusRow<>(list_ids.get(i), e));
			i++;
		}
		return finalList;
	}
	
	@Override
	public LinkedList<MarvusRow<K, E>> getRows(Comparator<MarvusRow<K, E>> comparator)
	{
		LinkedList<MarvusRow<K, E>> finalList = getRows();
		Collections.sort(finalList, comparator);
		return finalList;
	}

	@Override
	public Optional<E> getByID(K ID)
	{
		return Optional.ofNullable(data.get(ID));
	}

	@Override
	public List<E> getData()
	{
		return Collections.list(data.elements());
	}
}
