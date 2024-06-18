package riyufuchi.marvusLib.records;

import java.util.LinkedList;

import riyufuchi.marvus.controller.MarvusController;
import riyufuchi.marvus.windows.MarvusDataWindow;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;

/**
 * @author Riyufuchi
 * @since 11.02.2024
 * @version 17.06.2024
 */
public record FileInput(String fromFileType, LinkedList<?> data)
{
	@SuppressWarnings("unchecked")
	@Deprecated
	public void setDataTo(MarvusDataWindow mdw)
	{
		switch (fromFileType)
		{
			case ".dat" -> mdw.getController().setDatabase((MarvusDatabase)data.getFirst());
			default -> mdw.getController().getDatabase().addAll((LinkedList<Transaction>)data);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setDataTo(MarvusController mc)
	{
		switch (fromFileType)
		{
			case ".dat" -> mc.setDatabase((MarvusDatabase)data.getFirst());
			default -> mc.getDatabase().addAll((LinkedList<Transaction>)data);
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
