package workData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import gui.info.AppTexts;

/**
 * Created On: 10.09.2022<br>
 * Last Edit: 10.09.2022<hr>
 * This class is made for handling list (LinkedList) of Money class for now. 
 * @author Riyufuchi
 * @version 1.0
 * @since 1.0
 */
public final class DataBox
{
	private LinkedList<Money> data;
	private Comparator<Money> comparator;
	private Consumer<Exception> errorLoger;
	private SimpleDateFormat dateFormat;
	
	public DataBox(Consumer<Exception> errorLogerSetter, String dateRegex)
	{
		checkArguments(errorLogerSetter, dateRegex);
		this.data = new LinkedList<>();
		this.dateFormat = new SimpleDateFormat(dateRegex);
		//this.comparator = (m1, m2) -> m1.getDate().compareTo(m2.getDate());
		this.comparator = (m1, m2) -> {
			try
			{
				return dateFormat.parse(m1.getDate()).compareTo(dateFormat.parse(m2.getDate()));
			} 
			catch (ParseException e)
			{
				errorLoger.accept(e);
			}
			return 0;
		};
	}
	
	private void checkArguments(Consumer<Exception> errorLogerSetter, String dateRegex)
	{
		boolean dateRegexOK = false;
		if(dateRegex == null)
			dateRegex = AppTexts.DATE_FORMAT_OPTIONS[0];
		else
			for(String regex : AppTexts.DATE_FORMAT_OPTIONS)
				if(regex.equals(dateRegex))
				{
					dateRegexOK = true;
					return;
				}
		if(!dateRegexOK)
			dateRegex = AppTexts.DATE_FORMAT_OPTIONS[0];
		
		if(errorLogerSetter == null)
			this.errorLoger = e -> System.out.println(e.getMessage());
		else
			this.errorLoger = errorLogerSetter;
	}
	
	/*private void zpracujError(Exception e)
	{
		if(errorLoger == null)
			Logger.getLogger(Spravce.class.getName()).log(Level.SEVERE, null, e);
		else
			errorLoger.accept(Spravce.class.getName() + " " + e);
	}*/
	
	//UTIL METHODS
	
	public void sort()
	{
		if(data.isEmpty())
			return;
		Collections.sort(data, comparator);
	}
	
	//ADDING METHODS
	
	public void add(Money money)
	{
		if(money == null)
			return;
		data.add(money);
	}
	
	public void addMultiple(List<Money> newData) throws NullPointerException
	{
		Objects.requireNonNull(newData).stream().forEach(m -> data.add(m));
	}
	
	//COLLECTIONS UTILS
	
	public Iterator<Money> iterator()
	{
		return data.iterator();
	}
	
	public Stream<Money> stream()
	{
		return data.stream();
	}
	
	//IS METHODS
	
	public boolean isEmpty()
	{
		return data.isEmpty();
	}
	
	//SETTERS
	
	public void setList(LinkedList<Money> list)
	{
		data = list;
	}
	
	//GETTERS
	
	public List<Money> getList()
	{
		return data;
	}
}
