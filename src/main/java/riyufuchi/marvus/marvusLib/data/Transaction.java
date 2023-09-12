package riyufuchi.marvus.marvusLib.data;

import java.time.LocalDateTime;
import java.util.Objects;

import riyufuchi.marvus.marvusLib.legacy.DateUtils;

/**
 * Transaction class represents simple transaction<br><br>
 * 
 * Created On: 13.04.2023<br>
 * Last Edit: 12.09.2023

 * @author Riyufuchi
 * @version 1.8
 * @since 1.22
 */
public class Transaction extends Money
{
	private static final long serialVersionUID = 1L;
	private static int Static_ID = 0;
	private final int ID;
	private String name;
	private String category;
	private String note;
	// Date
	private String dateString;
	private LocalDateTime date;
	
	/**
	 * Creates a dummy transaction<br><br>
	 * ID: -1<br>
	 * Name: DummyTransaction<br>
	 * Category: None<br>
	 * Value: 0<br>
	 * Currency: DummyUnits<br>
	 * Date: 1.1.2018<br>
	 * Note: Note<br>
	 */
	public Transaction()
	{
		super("0", "DummyUnits");
		this.ID = -1;
		this.name = "DummyTransaction";
		this.category = "None";
		this.note = "Note";
		setDate("1.1.2018");
	}
	
	public Transaction(String name, String category, String sum, String date, String note)
	{
		super(sum);
		this.ID = ++Static_ID;
		this.name = name;
		this.category = category;
		this.note = note;
		setDate(date);
	}
	
	public Transaction(String name, String category, String sum, String currency, String date, String note)
	{
		super(sum, currency);
		this.ID = ++Static_ID;
		this.name = name;
		this.category = category;
		this.note = note;
		setDate(date);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(ID, category, date, dateString, name, note);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof Transaction))
			return false;
		Transaction t = (Transaction)o;
		if (ID == t.getID())
			return true;
		//if (name.equals(t.getName()) && note.equals(t.getNote()) && category.equals(t.getCategory()))
			//if (getValue().equals(t.getValue()))
				//if (date.equals(t.date))
					//return true;
		return false;
	}
	
	//SETTERS
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public void setNote(String note)
	{
		this.note = note;
	}
	
	public void setDate(String date)
	{
		this.dateString = DateUtils.dateToString(date);
		this.date = DateUtils.toLocalDateTime(dateString);
	}
	
	//GETTERS
	
	public int getID()
	{
		return ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public String getNote()
	{
		return note;
	}
	
	public LocalDateTime getDate()
	{
		return date;
	}
	
	public String getStringDate()
	{
		return dateString;
	}

	@Override
	public String toString()
	{
		return name + ";" + category +";" + super.toString() + ";" + dateString + ";" + note;
	}

	@Override
	public String toCSV()
	{
		return name + ";" + category +";" + super.toString() + ";" + dateString + ";" + note;
	}
	
	@Override
	public Transaction fromCSV(String[] data)
	{
		return new Transaction(data[0], data[1], data[2], data[3], data[4], data[5]);
	}
}
