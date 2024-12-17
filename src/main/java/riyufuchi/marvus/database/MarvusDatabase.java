package riyufuchi.marvus.database;

import java.util.function.Consumer;

import javax.swing.JFrame;

import riyufuchi.marvusLib.database.MarvusDatabaseTable;
import riyufuchi.marvusLib.database.MarvusMainTable;
import riyufuchi.marvusLib.database.MarvusTableDB;
import riyufuchi.marvusLib.records.TransactionMacro;

/**
 * This class doesn't represent actual connection to database, just "simulates" it
 * 
 * @author Riyufuchi
 * @since 12.12.2024
 * @version 12.12.2024
 */
public class MarvusDatabase extends MarvusMainTable
{
	private MarvusDatabaseIO mdbio;
	public MarvusTableDB<String, TransactionMacro> macroTable;
	public MarvusDatabaseTable<String> entities;
	public MarvusDatabaseTable<String> categories;
	
	public MarvusDatabase()
	{
		this(e -> System.out.println(e), null);
	}
	
	public MarvusDatabase(Consumer<String> errorHandler, JFrame frame)
	{
		super(errorHandler);
		this.mdbio = new MarvusDatabaseIO(frame);
		this.macroTable = mdbio.loadTransactionMacroTable();
		this.entities = mdbio.loadEntityTable();
		this.categories = mdbio.loadCategoryTable();
	}
}
