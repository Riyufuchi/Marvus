package riyufuchi.marvus.marvusData;

/**
 * Created On: 13.04.2023<br>
 * Last Edit: 17.05.2023
 * <hr>
 * Transaction class represents simple transaction
 * <hr>
 * @author Riyufuchi
 * @version 1.1
 * @since 1.22
 */
public class Transaction extends MoneySum
{
	private static final long serialVersionUID = 1L;
	private String name;
	private String note;
	
	public Transaction(String name, String sum, String date, String note)
	{
		super(sum, date);
		this.name = name;
		this.note = note;
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
			if (getMoneySum().equals(t.getMoneySum()))
				if (getDate().equals(t.getDate()))
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
	
	//GETTERS
	
	public String getName()
	{
		return name;
	}
	
	public String getNote()
	{
		return note;
	}

	@Override
	public String toString()
	{
		return name + ";" + super.toString() + ";" + note;
	}
}
