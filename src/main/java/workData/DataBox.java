package workData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	public DataBox()
	{
		this.data = new LinkedList<>();
		//this.comparator = (m1, m2) -> m1.getDate().compareTo(m2.getDate());
		this.comparator = (m1, m2) -> {
			try {
				return dateFormat.parse(m1.getDate()).compareTo(dateFormat.parse(m2.getDate()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		};
	}
	
	public void add(Money money)
	{
		data.add(money);
	}
	
	public void sort()
	{
		if(data.isEmpty())
			return;
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
