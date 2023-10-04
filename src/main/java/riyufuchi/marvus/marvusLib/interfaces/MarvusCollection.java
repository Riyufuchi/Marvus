package riyufuchi.marvus.marvusLib.interfaces;

import java.util.Collection;
import java.util.LinkedList;

public interface MarvusCollection<E> extends Collection<E>
{
	LinkedList<E> toList();
}
