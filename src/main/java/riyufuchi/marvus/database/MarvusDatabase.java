package riyufuchi.marvus.database;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.FinancialCategorySafe;
import riyufuchi.marvusLib.database.MarvusMainTable;
import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;
import riyufuchi.marvusLib.records.MarvusCategoryStatistic;
import riyufuchi.marvusLib.records.MarvusDataStatistics;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.marvusLib.records.MarvusYearOverview;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.interfaces.SufuIDatabase;
import riyufuchi.sufuLib.time.SufuDateUtils;

/**
 * This class doesn't represent actual connection to database, just "simulates" it
 * 
 * @author Riyufuchi
 * @since 12.12.2024
 * @version 09.05.2025
 */
public class MarvusDatabase implements MarvusDatabaseController, Serializable
{
	private static final long serialVersionUID = 10L;
	private MarvusDatabaseIO databaseIO;
	private SufuTableDB<String, TransactionMacro> macroTable;
	private SufuTableDB<Integer, String> entities;
	private SufuTableDB<Integer, String> categories;
	private MarvusMainTable transactionTable;
	
	public MarvusDatabase()
	{
		this(e -> System.out.println(e), null);
	}
	
	public MarvusDatabase(Consumer<String> errorHandler, JFrame frame)
	{
		this.transactionTable = new MarvusMainTable(errorHandler);
		this.databaseIO = new MarvusDatabaseIO(frame);
		this.macroTable = databaseIO.loadTransactionMacroTable();
		this.entities = databaseIO.loadEntityTable();
		this.categories = databaseIO.loadCategoryTable();
	}
	
	@Override
	public boolean createBackup()
	{
		try
		{
			if (categories.getCount() != 0)
				SufuPersistence.saveToCSVtoString(MarvusConfig.CATEGORY_FILE_PATH, categories.getData());
			if (entities.getCount() != 0)
				SufuPersistence.saveToCSVtoString(MarvusConfig.ENTITY_FILE_PATH, entities.getData());
			if (macroTable.getCount() != 0)
				SufuPersistence.saveToCSV(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, macroTable.getData());
		}
		catch (NullPointerException | IOException e)
		{
			transactionTable.getErrorHandler().accept(e.getLocalizedMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public boolean insertTransaction(Transaction transaction)
	{
		return transactionTable.add(transaction);
	}
	
	@Override
	public boolean insertCategory(String category)
	{
		if (categories.add(category))
			return databaseIO.saveTableToFile(MarvusConfig.CATEGORY_TABLE_PATH, categories);
		return false;
	}

	@Override
	public boolean insertEntity(String name)
	{
		if (entities.add(name))
			return databaseIO.saveTableToFile(MarvusConfig.ENTITY_TABLE_PATH, entities);
		return false;
	}
	
	@Override
	public boolean updateTransaction(Transaction transaction)
	{
		return transactionTable.set(transaction);
	}
	
	@Override
	public boolean updateCategory(int categoryID, String replacementCategory)
	{
		String oldValue = categories.getByID(categoryID).get();
		if (categories.set(categoryID, replacementCategory))
			databaseIO.saveTableToFile(MarvusConfig.CATEGORY_TABLE_PATH, categories);
		else
			return false;
		return updateAtribbute("Category", oldValue, replacementCategory);
	}

	@Override
	public boolean updateEntity(int nameID, String replacementName)
	{
		String oldValue = entities.getByID(nameID).get();
		if (entities.set(nameID, replacementName))
			databaseIO.saveTableToFile(MarvusConfig.ENTITY_TABLE_PATH, entities);
		else
			return false;
		return updateAtribbute("Name", oldValue, replacementName);
	}

	@Override
	public boolean removeTransaction(Transaction transactionID)
	{
		return transactionTable.remove(transactionID);
	}
	
	@Override
	public boolean removeCategory(int categoryID, int replacementCategoryID)
	{
		String oldValue = categories.getByID(categoryID).get();
		String newValue = categories.getByID(replacementCategoryID).get();
		if (categories.delete(categoryID))
			databaseIO.saveTableToFile(MarvusConfig.ENTITY_TABLE_PATH, categories);
		else
			return false;
		return updateAtribbute("Category", oldValue, newValue);
	}

	@Override
	public boolean removeEntity(int nameID, int replacementNameID)
	{
		String oldValue = entities.getByID(nameID).get();
		String newValue = entities.getByID(replacementNameID).get();
		if (entities.delete(nameID))
			databaseIO.saveTableToFile(MarvusConfig.ENTITY_TABLE_PATH, entities);
		else
			return false;
		return updateAtribbute("Name", oldValue, newValue);
	}
	
	@Override
	public boolean updateAtribbute(String atr, String oldValue, String newValue)
	{
		switch (atr)
		{
			case "Name" -> transactionTable.stream().filter(t -> t.getName().equals(oldValue)).forEach(t -> t.setName(newValue));
			case "Category" -> transactionTable.stream().filter(t -> t.getCategory().equals(oldValue)).forEach(t -> t.setCategory(newValue));
			default -> { updateAll(createStreamFor(atr, oldValue), createPredicateFor("", ""), createConsumerFor(atr, newValue)); }
		}
		return true;
	}
	
	@Override
	public boolean updateAtribbute(String whereAttr, String whereValue, String targetAttr, String oldValue, String newValue)
	{
		if (whereAttr == null || whereValue == null || targetAttr == null || newValue == null || oldValue == null)
			return false;
		updateAll(createStreamFor(whereAttr, whereValue), createPredicateFor(targetAttr, oldValue), createConsumerFor(targetAttr, newValue));
		return true;
	}
	
	private Stream<Transaction> createStreamFor(String targetAttr, String whereValue)
	{
		return transactionTable.stream().filter(createPredicateFor(targetAttr, whereValue));
	}
	
	private Predicate<Transaction> createPredicateFor(String targetAttr, String whereValue)
	{
		if (whereValue.isBlank())
			return t -> true;
		switch (targetAttr)
		{
			case "Name" -> { return t -> t.getName().equals(whereValue); }
			case "Category" -> { return t -> t.getCategory().equals(whereValue); }
			default -> { return t -> false; }
		}
	}
	
	private Consumer<Transaction> createConsumerFor(String targetAttr, String newValue)
	{
		switch (targetAttr)
		{
			case "" -> { return t -> {}; }
			case "Name" -> { return t -> t.setName(newValue); }
			case "Category" -> { return t -> t.setCategory(newValue); }
			default -> { return t -> {}; }
		}
	}
	
	private void updateAll(Stream<Transaction> stream, Predicate<Transaction> predicate, Consumer<Transaction> consumer)
	{
		stream.filter(predicate).forEach(consumer);
	}
	
	@Override
	public MarvusDataStatistics createDataStatistics(int year)
	{
		final BigDecimal TOTAL_TRANSACTION = new BigDecimal(transactionTable.size());
		final BigDecimal TWELVE = new BigDecimal(12);
		BigDecimal numOfDays = new BigDecimal(365);
		if (SufuDateUtils.isLeapYear(year))
			numOfDays.add(BigDecimal.ONE);
		// Average transactions
		BigDecimal avgYearTransactions = TOTAL_TRANSACTION.divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgMonthTransactions = TOTAL_TRANSACTION.divide(numOfDays, 2, RoundingMode.HALF_UP);
		// Average income, outcome and total
		MarvusYearOverview yo = transactionTable.getYearOverview(year);
		BigDecimal avgIncome = yo.totalIncome().divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgSpendings = yo.totalSpendings().divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgTotal = yo.totalResult().divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgDailyIncome = yo.totalIncome().divide(numOfDays, 2, RoundingMode.HALF_UP);
		BigDecimal avgDailySpendings = yo.totalSpendings().divide(numOfDays, 2, RoundingMode.HALF_UP);
		BigDecimal avgDailyTotal = yo.totalResult().divide(numOfDays, 2, RoundingMode.HALF_UP);
		
		return new MarvusDataStatistics(TOTAL_TRANSACTION.intValue(), avgYearTransactions, avgMonthTransactions,
				yo.totalIncome(), yo.totalSpendings(), yo.totalResult(),
				avgIncome, avgSpendings, avgTotal,
				avgDailyIncome, avgDailySpendings, avgDailyTotal);
	}

	@Override
	public MarvusCategoryStatistic createCategoryStatistic(String category, int year)
	{
		FinancialCategorySafe theOne = null;
		for (FinancialCategorySafe financialCategory : getCategorizedYearByCategories(year))
		{
			if (financialCategory.getCategory().equals(category))
			{
				theOne = financialCategory;
				break;
			}
		}
		if (theOne == null)
			return  new MarvusCategoryStatistic(category, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
		final BigDecimal TWELVE = new BigDecimal(12);
		BigDecimal numOfDays = new BigDecimal(365);
		if (SufuDateUtils.isLeapYear(year))
			numOfDays.add(BigDecimal.ONE);
		BigDecimal theOneSum = theOne.getSum();
		BigDecimal yearAvg = theOneSum.divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal dailyAvg = theOneSum.divide(numOfDays, 2, RoundingMode.HALF_UP);
		return new MarvusCategoryStatistic(category, theOneSum, yearAvg, dailyAvg);
	}

	@Override
	public SufuIDatabase<String, TransactionMacro> getMacrosTable()
	{
		return macroTable;
	}

	@Override
	public SufuIDatabase<Integer, Transaction> getTransactionsTable()
	{
		return transactionTable;
	}

	@Override
	public SufuIDatabase<Integer, String> getCategoriesTable()
	{
		return categories;
	}

	@Override
	public SufuIDatabase<Integer, String> getEntitiesTable()
	{
		return entities;
	}

	@Override
	public MarvusYearOverview createYearOverview(int year)
	{
		return transactionTable.getYearOverview(year);
	}

	@Override
	public LinkedList<FinancialCategorySafe> getCategorizedMonthByNames(Month month)
	{
		return transactionTable.getCategorizedMonthByNames(month);
	}

	@Override
	public LinkedList<FinancialCategorySafe> getCategorizedYearByCategories(int year)
	{
		return transactionTable.getCategorizedYearByCategories(year);
	}

	@Override
	public LinkedList<FinancialCategorySafe> getCategorizedMonth(Month month)
	{
		return transactionTable.getCategorizedMonth(month);
	}

	@Override
	public LinkedList<Transaction> getMonth(Month month)
	{
		return transactionTable.getMonth(month);
	}

	@Override
	public boolean insertAllTransactions(Collection<Transaction> transactionList)
	{
		return transactionTable.addAll(transactionList);
	}

	@Override
	public int assumeYear()
	{
		Transaction t = transactionTable.getByID(1).orElse(new Transaction());
		if (t.getID() == -1)
			return -1;
		return t.getDate().getYear();
	}
	
	public Stream<Transaction> stream()
	{
		return transactionTable.stream();
	}

	public void setErrorHandler(Consumer<String> object)
	{
		transactionTable.setErrorHandler(object);
	}
}
