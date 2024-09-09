package riyufuchi.marvusLib.database;

import java.math.BigDecimal;

import riyufuchi.marvusLib.interfaces.MarvusQuerriable;

/**
 * @author riyufuchi
 * @since 09.09.2024
 * @version 09.09.2024
 */
public class MarvusConnection implements MarvusQuerriable
{
	private MarvusDatabase	database;

	public MarvusConnection(MarvusDatabase databse)
	{
		this.database = databse;
	}

	@Override
	public boolean updateAtribbute(String attr, String oldValue, String newValue)
	{
		updateName(oldValue, newValue);
		return true;
	}
	
	private void updateName(String oldValue, String newValue)
	{
		database.stream().forEach(e -> {
			if (e.getName().equals(oldValue))
				e.setName(newValue);
		});
	}

	@Override
	public boolean updateItemWhere(String whereAttr, String whereValue, String targetAttr, String newValue)
	{
		if (whereAttr == null || whereValue == null || newValue == null)
			return false;
		switch (whereAttr)
		{
			case "value" -> updateNameWhenValue(new BigDecimal(whereValue), newValue);
			default -> { return false; }
		}
		return true;
	}
	
	private void updateNameWhenValue(BigDecimal value, String name)
	{
		database.stream().forEach(e -> {
			if (e.getValue().compareTo(value) == 0)
				e.setName(name);
		});
	}
}
