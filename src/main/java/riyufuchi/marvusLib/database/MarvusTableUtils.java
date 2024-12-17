package riyufuchi.marvusLib.database;

import java.util.LinkedList;
import java.util.List;

import riyufuchi.marvusLib.records.MarvusRow;
import riyufuchi.marvusLib.records.Row;
import riyufuchi.marvusLib.records.TransactionMacro;

public final class MarvusTableUtils
{
	public static List<String> selectOrdered(List<String> table)
	{
		table.sort( (t1, t2) -> { return t1.compareTo(t2); });
		return table;
	}
	
	public static LinkedList<Row<String>> selectOrdered(MarvusDatabaseTable<String> table)
	{
		return table.getRows((t1, t2) -> t1.entity().compareTo(t2.entity()));
	}
	
	public static LinkedList<MarvusRow<String, TransactionMacro>> selectMacroOrdered(MarvusTableDB<String, TransactionMacro> macros)
	{
		return macros.getRows((t1, t2) -> t1.entity().name().compareTo(t2.entity().name()));
	}
}
