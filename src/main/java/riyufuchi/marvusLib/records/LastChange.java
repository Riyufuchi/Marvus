package riyufuchi.marvusLib.records;

import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.enums.UserAction;

public record LastChange(UserAction userAction, Transaction transactionInQuestion)
{
}
