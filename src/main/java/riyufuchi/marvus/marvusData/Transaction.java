package riyufuchi.marvus.marvusData;

import java.time.LocalDateTime;

import riyufuchi.marvus.marvusLib.utils.DateUtils;

/**
 * Transaction class represents simple transaction<br><br>
 * 
 * Created On: 13.04.2023<br>
 * Last Edit: 06.08.2023

 * @author Riyufuchi
 * @version 1.4
 * @since 1.22
 */
public class Transaction extends Money
{
	private static final long serialVersionUID = 1L;
	private static int Static_ID = 0;
	private int ID;
	private String name;
	private String note;
	// Date
	private String dateString;
	private LocalDateTime date;
	
	/**
	 * Create a dummy transaction<br>
	 * ID: -1<br>
	 * Name: DummyTransaction<br>
	 * Value: 0<br>
	 * Currency: DummyUnits<br>
	 * Note: Note<br>
	 */
	public Transaction()
	{
		super("0", "DummyUnits");
		this.ID = -1;
		this.name = "DummyTransaction";
		this.note = "Note";
		setDate("1.1.2018");
	}
	
	public Transaction(String name, String sum, String date, String note)
	{
		super(sum);
		this.ID = ++Static_ID;
		this.name = name;
		this.note = note;
		setDate(date);
	}
	
	public Transaction(String name, String sum, String currency, String date, String note)
	{
		super(sum, currency);
		this.ID = ++Static_ID;
		this.name = name;
		this.note = note;
		setDate(date);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		if (!(o instanceof Transaction))
			return false;
		Transaction t = (Transaction)o;
		if (name.equals(t.getName()) && note.equals(t.getNote()))
			if (getValue().equals(t.getValue()))
				if (date.equals(t.date))
					return true;
		return false;
	}

	//SETTERS
	
	public void setName(String name)
	{
		this.name = name;
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
		return name + ";" + super.toString() + ";" + dateString + ";" + note;
	}

	@Override
	public String toCSV()
	{
		return name + ";" + super.toString() + ";" + dateString + ";" + note;
	}
	
	@Override
	public Transaction fromCSV(String[] data)
	{
		return new Transaction(data[0], data[1], data[2], data[3], data[4]);
	}
}
