package riyufuchi.marvus.marvusLib.dataBase;

import java.util.function.Consumer;

import riyufuchi.marvus.marvusLib.dataStorage.MarvusDataTable;

/**
 * This class doesn't represent actual connection to database, just "simulates" it
 * 
 * @author Riyufuchi
 * @version 07.10.2023
 * @since 07.10.2023
 */
public class MarvusDatabase extends MarvusDataTable
{
	public static MaruvsDatabaseUtils utils = new MaruvsDatabaseUtils();
	
	public MarvusDatabase(Consumer<String> errorHandler)
	{
		super(errorHandler);
	}

}
