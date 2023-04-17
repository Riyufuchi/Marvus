package riyufuchi.marvus.marvusData;

/**
 * Created On: 13.04.2023<br>
 * Last Edit: 13.04.2023
 * <hr>
 * Transaction class represents simple transaction
 * <hr>
 * @author Riyufuchi
 * @version 1.0
 * @since 1.22
 */
public class Transaction extends MoneySum
{
	private static final long serialVersionUID = 1L;
	private String name;
	
	public Transaction(String name, String sum, String date)
	{
		super(sum, date);
		this.name = name;
	}

	//SETTERS
	
	public void setName(String name) {
		this.name = name;
	}
	
	//GETTERS
	
	public String getName() {
		return name;
	}

	@Override
	public String toString()
	{
		return name + ";" + super.toString();
	}
}
