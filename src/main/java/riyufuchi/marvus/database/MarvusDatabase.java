package riyufuchi.marvus.database;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JFrame;

import riyufuchi.marvus.app.MarvusConfig;
import riyufuchi.marvusLib.data.Transaction;
import riyufuchi.marvusLib.dataUtils.FinancialCategory;
import riyufuchi.marvusLib.database.MarvusDatabaseTable;
import riyufuchi.marvusLib.database.MarvusMainTable;
import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;
import riyufuchi.marvusLib.records.MarvusCategoryStatistic;
import riyufuchi.marvusLib.records.MarvusDataStatistics;
import riyufuchi.marvusLib.records.TransactionMacro;
import riyufuchi.marvusLib.records.YearOverview;
import riyufuchi.sufuLib.database.SufuTableDB;
import riyufuchi.sufuLib.files.SufuPersistence;
import riyufuchi.sufuLib.interfaces.IDatabase;
import riyufuchi.sufuLib.interfaces.SufuITableDB;
import riyufuchi.sufuLib.time.SufuDateUtils;


/**
 * This class doesn't represent actual connection to database, just "simulates" it
 * 
 * @author Riyufuchi
 * @since 12.12.2024
 * @version 11.01.2025
 */
public class MarvusDatabase extends MarvusMainTable implements MarvusDatabaseController
{
	private MarvusDatabaseIO mdbio;
	private SufuTableDB<String, TransactionMacro> macroTable;
	private MarvusDatabaseTable<String> entities;
	private MarvusDatabaseTable<String> categories;
	
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
	
	public void createBackup()
	{
		try
		{
			if (categories.getCount() != 0)
				SufuPersistence.saveToCSVtoString(MarvusConfig.CATEGORY_FILE_PATH, categories.getData());
			if (entities.getCount() != 0)
				SufuPersistence.saveToCSVtoString(MarvusConfig.ENTITY_FILE_PATH, entities.getData());
			SufuPersistence.saveToCSV(MarvusConfig.TRANSACTION_MACRO_FILE_PATH, macroTable.getData());
		}
		catch (NullPointerException | IOException e)
		{
			errorHandler.accept(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean removeCategory(int categoryID, int replacementCategoryID)
	{
		return false;
	}

	@Override
	public boolean removeEntity(int nameID, int replacementNameID)
	{
		return false;
	}
	
	@Override
	public boolean updateAtribbute(String atr, String oldValue, String newValue)
	{
		switch (atr)
		{
			case "Name" -> stream().filter(t -> t.getName().equals(oldValue)).forEach(t -> t.setName(newValue));
			case "Category" -> stream().filter(t -> t.getCategory().equals(oldValue)).forEach(t -> t.setCategory(newValue));
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
		return stream().filter(createPredicateFor(targetAttr, whereValue));
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
		final BigDecimal TOTAL_TRANSACTION = new BigDecimal(size());
		final BigDecimal TWELVE = new BigDecimal(12);
		BigDecimal numOfDays = new BigDecimal(365);
		if (SufuDateUtils.isLeapYear(year))
			numOfDays.add(BigDecimal.ONE);
		// Average transactions
		BigDecimal avgYearTransactions = TOTAL_TRANSACTION.divide(TWELVE, 2, RoundingMode.HALF_UP);
		BigDecimal avgMonthTransactions = TOTAL_TRANSACTION.divide(numOfDays, 2, RoundingMode.HALF_UP);
		// Average income, outcome and total
		YearOverview yo = getYearOverview(year);
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
		FinancialCategory theOne = null;
		for (FinancialCategory financialCategory : getCategorizedYearByCategories(year))
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
	public SufuITableDB<String, TransactionMacro> getMacrosTableController()
	{
		return macroTable;
	}

	@Override
	public IDatabase<Transaction> getTransactionsTableController()
	{
		return this;
	}

	@Override
	public IDatabase<String> getCategoriesTableController()
	{
		return categories;
	}

	@Override
	public IDatabase<String> getEntitiesTableController()
	{
		return entities;
	}
}
