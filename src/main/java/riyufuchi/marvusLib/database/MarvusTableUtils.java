package riyufuchi.marvusLib.database;

import java.util.LinkedList;

import riyufuchi.marvusLib.records.MarvusRow;
import riyufuchi.marvusLib.records.TransactionMacro;

public final class MarvusTableUtils
{
	public static LinkedList<MarvusRow<String, TransactionMacro>> selectMacroOrdered(MarvusTableDB<String, TransactionMacro> macros)
	{
		return macros.getRows((t1, t2) -> t1.entity().name().compareTo(t2.entity().name()));
	}
}
