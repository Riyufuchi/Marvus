package riyufuchi.marvus.marvusLib.records;

import java.util.LinkedList;

import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.marvus.marvusLib.data.Transaction;
import riyufuchi.marvus.marvusLib.database.MarvusDatabase;

/**
 * 
 * @author Riyufuchi
 * @since 11.02.2024
 * @version 12.02.2024
 */
public record FileInput(String fromFileType, LinkedList<?> data)
{
	@SuppressWarnings("unchecked")
	public void setDataTo(MarvusDataWindow mdw)
	{
		switch (fromFileType)
		{
			case ".dat" -> mdw.getController().setDatabase((MarvusDatabase)data.getFirst());
			default -> mdw.getController().getDatabase().addAll((LinkedList<Transaction>)data);
		}
	}
	
	@SuppressWarnings("unchecked")
	public MarvusDatabase convertDataToDB()
	{
		switch (fromFileType)
		{
			case ".dat" -> { return (MarvusDatabase)data.getFirst(); }
			default -> {
				MarvusDatabase database = new MarvusDatabase();
				database.addAll((LinkedList<Transaction>)data);
				return database;
			}
		}
	}
}
