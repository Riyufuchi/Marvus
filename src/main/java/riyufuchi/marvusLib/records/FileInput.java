package riyufuchi.marvusLib.records;

import java.util.LinkedList;

import riyufuchi.marvus.app.MarvusController;
import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.database.MarvusDatabase;

/**
 * @author Riyufuchi
 * @since 11.02.2024
 * @version 12.08.2024
 */
public record FileInput(String fromFileType, LinkedList<?> data)
{
	@SuppressWarnings("unchecked")
	@Deprecated
	public void setDataTo(MarvusDataWindow mdw)
	{
		switch (fromFileType)
		{
			case MarvusConfig.MDB_EXT -> mdw.getController().setDatabase((MarvusDatabase)data.getFirst());
			default -> mdw.getController().getDatabase().addAll((LinkedList<Transaction>)data);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setDataTo(MarvusController mc)
	{
		switch (fromFileType)
		{
			case MarvusConfig.MDB_EXT -> { 
				mc.setDatabase((MarvusDatabase)data.getFirst());
				//mc.getDatabase().sort();
				}
			default -> mc.getDatabase().addAll((LinkedList<Transaction>)data);
		}
	}
	
	@SuppressWarnings("unchecked")
	public MarvusDatabase convertDataToDB()
	{
		switch (fromFileType)
		{
			case MarvusConfig.MDB_EXT -> { return (MarvusDatabase)data.getFirst(); }
			default -> {
				MarvusDatabase database = new MarvusDatabase();
				database.addAll((LinkedList<Transaction>)data);
				return database;
			}
		}
	}
}
