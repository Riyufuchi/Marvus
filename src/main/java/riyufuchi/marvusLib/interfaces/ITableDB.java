package riyufuchi.marvusLib.interfaces;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import riyufuchi.marvusLib.records.MarvusRow;

/**
 * Good for database controllers or data structures simulating database
 * 
 * @param <E>
 */
public interface ITableDB<K, E>
{
	boolean add(final K ID, E e);
	boolean remove(final K ID);
	boolean set(final K ID, E e);
	Optional<E> getByID(final K ID);
	List<E> getData();
	LinkedList<MarvusRow<K, E>> getRows();
	LinkedList<MarvusRow<K, E>> getRows(Comparator<MarvusRow<K, E>> comparator);
}
