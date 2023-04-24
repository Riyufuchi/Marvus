package riyufuchi.marvus.marvusData;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created On: 10.09.2022<br>
 * Last Edit: 24.04.2023
 * <hr>
 * Class for managing LinkedList and data interactions
 * <hr>
 * @author Riyufuchi
 * @version 1.7
 * @since 1.0
 */
public final class DataBox<E extends MoneySum> implements Iterable<E>
{
	private LinkedList<E> data;
	private Comparator<E> comparator;
	private Consumer<Exception> errorLoger;
	
	public DataBox(Consumer<Exception> errorLoger, Comparator<E> comparator)
	{
		checkArguments(errorLoger, comparator);
		this.data = new LinkedList<>();
	}
	
	private void checkArguments(Consumer<Exception> errorLoger, Comparator<E> comparator)
	{
		if (errorLoger == null)
			this.errorLoger = e -> Logger.getLogger(DataBox.class.getName()).log(Level.SEVERE, null, e);
		else
			this.errorLoger = errorLoger;
		
		if (comparator == null)
			this.comparator = (o1, o2) -> { return o1.getMoneySum().compareTo(o2.getMoneySum()); };
		else
			this.comparator = comparator;
	}
	
	//UTIL METHODS
	
	public void sort()
	{
		if (data.isEmpty())
			return;
		Collections.sort(data, comparator);
	}
	
	//ADDING METHODS
	
	public void add(E money)
	{
		if (money == null)
			return;
		data.add(money);
	}
	
	public void addMultiple(List<E> newData) //throws NullPointerException
	{
		if (newData == null)
		{
			errorLoger.accept(new NullPointerException());
			return;
		}
		newData.stream().forEach(m -> data.add(m));
	}
	
	//COLLECTIONS UTILS
	
	public Iterator<E> iterator()
	{
		return data.iterator();
	}
	
	public Stream<E> stream()
	{
		return data.stream();
	}
	
	//IS METHODS
	
	public boolean isEmpty()
	{
		return data.isEmpty();
	}
	
	//SETTERS
	
	public void setList(LinkedList<E> list) //throws NullPointerException
	{
		if (list == null)
		{
			errorLoger.accept(new NullPointerException());
			return;
		}
		data = list;
	}
	
	public void setComparator(Comparator<E> comp) //throws NullPointerException
	{
		if (comp == null)
		{
			errorLoger.accept(new NullPointerException());
			return;
		}
		this.comparator = comp;
	}
	
	//GETTERS
	
	public LinkedList<E> getList()
	{
		return data;
	}
}
