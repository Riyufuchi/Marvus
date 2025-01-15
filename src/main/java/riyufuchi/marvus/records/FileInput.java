package riyufuchi.marvus.records;

import java.util.LinkedList;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvus.controller.TabController;
import riyufuchi.marvus.database.MarvusDatabase;
import riyufuchi.marvusLib.data.Transaction;

/**
 * @author Riyufuchi
 * @since 11.02.2024
 * @version 15.01.2025
 */
public record FileInput(String fromFileType, LinkedList<?> data)
{
	@SuppressWarnings("unchecked")
	public void setDataTo(TabController mc)
	{
		switch (fromFileType)
		{
			case MarvusConfig.MDB_EXT -> mc.setDatabase((MarvusDatabase)data.getFirst());
			default -> mc.getDatabase().insertAllTransactions((LinkedList<Transaction>)data);
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
