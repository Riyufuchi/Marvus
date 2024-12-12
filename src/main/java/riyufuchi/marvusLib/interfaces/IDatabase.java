package riyufuchi.marvusLib.interfaces;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import riyufuchi.marvusLib.records.Row;

/**
 * Good for database controllers or data structures simulating database
 * 
 * @param <E>
 */
public interface IDatabase<E>
{
	boolean add(E e);
	boolean remove(final int ID);
	boolean set(final int ID, E e);
	Optional<E> getByID(final int ID);
	List<E> getData();
	LinkedList<Row<E>> getRows();
}
