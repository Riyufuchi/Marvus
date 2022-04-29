package workData;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class DataBox
{
	private LinkedList<Money> data;
	private Comparator<Money> comparator;
	
	public DataBox()
	{
		this.data = new LinkedList<>();
		this.comparator = (m1, m2) -> m1.getDate().compareTo(m2.getDate());
	}
	
	public void add(Money money)
	{
		data.add(money);
	}
	
	public void sort()
	{
		Collections.sort(data, comparator);
	}
	
	public void addMultiple(List<Money> newData) throws NullPointerException
	{
		Objects.requireNonNull(newData).stream().forEach(m -> data.add(m));
	}
	
	public Iterator<Money> iterator()
	{
		return data.iterator();
	}
	
	public Stream<Money> stream()
	{
		return data.stream();
	}
	
	public List<Money> getList()
	{
		return data;
	}
	
	public void setList(LinkedList<Money> list)
	{
		data = list;
	}
}
