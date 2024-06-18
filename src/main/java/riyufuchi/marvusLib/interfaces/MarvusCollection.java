package riyufuchi.marvusLib.interfaces;

import java.util.Collection;
import java.util.LinkedList;

public interface MarvusCollection<E> extends Collection<E>
{
	/**
	 * Converts collection into LinkedList
	 * 
	 * @return linked list containing all of the elements in this collection
	 */
	LinkedList<E> toList();
	
	boolean set(E e);
}
