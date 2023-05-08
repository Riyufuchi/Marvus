package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 05.05.2023<br>
 * Last Edit: 05.05.2023
 * 
 * @author Riyufuchi
 */
public class EditGridDialog extends MarvusGridDialog
{
	private JTextField name, money, date;
	private JComboBox<String> category;
	private Transaction transaction;
	
	public EditGridDialog(BudgetDataTable bdt, Transaction transaction)
	{
		super("Edit transaction", bdt);
		this.transaction = transaction;
		prefillComponents();
		setTitle(getTitle() + " " + transaction.getID());
	}
	
	private void prefillComponents()
	{
		category.setSelectedItem(transaction.getName());
		name.setText(transaction.getName());
		money.setText(transaction.getMoneySum().toString());
		date.setText(transaction.getStringDate());
	}
	
	private void generateCategoryList(String path)
	{
		try
		{
			FileHelper.checkFile(path);
			Persistance.saveToCSV(path, new String[]{"Custom"});
			DialogHelper.informationDialog(bdt, "Generated default " + path, "Category list fixer info");
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(bdt, e);
		}
	}
	
	@Override
	protected void createInputs(JPanel pane)
	{
		String path = "data/category.txt";
		String[] categoryList = { "Custom" }; 
		try
		{
			List<String> l = Persistance.loadTXT(path);
			categoryList = new String[l.size()];
			int i = 0;
			for (String s : l)
				categoryList[i++] = s;
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(bdt, e);
			generateCategoryList(path);
		}
		category = FactoryComponent.<String>createCombobox(categoryList);
		name = FactoryComponent.newTextField("");
		money = FactoryComponent.newTextField("");
		date = FactoryComponent.newTextField(DateUtils.nowDateString());
		category.addActionListener(evt -> {
			if (category.getItemAt(category.getSelectedIndex()).equals("Custom"))
			{
				name.setEnabled(true);
				name.setText("");
			}
			else
			{
				name.setEnabled(false);
				name.setText(category.getItemAt(category.getSelectedIndex()));
			}
		});
		// Set labels
		pane.add(new JLabel("Category:"), getGBC(0, 0));
		pane.add(new JLabel("Amount:"), getGBC(0, 2));
		pane.add(new JLabel("Date:"), getGBC(0, 3));
		// Set components
		pane.add(category, getGBC(1, 0));
		pane.add(name, getGBC(1, 1));
		pane.add(money, getGBC(1, 2));
		pane.add(date, getGBC(1, 3));
	}

	@Override
	protected Consumer<Integer> consume()
	{
		return con -> {
			transaction.setName(name.getText());
			transaction.setMoneySum(money.getText());
			transaction.setDate(date.getText());
			//bdt.getDataBox().add(new Transaction(name.getText(), money.getText(), date.getText()));
			bdt.refresh();
		};
	}
}
