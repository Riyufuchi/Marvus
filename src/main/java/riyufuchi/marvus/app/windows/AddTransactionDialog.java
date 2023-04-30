package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 24.04.2023
 * 
 * @author Riyufuchi
 */
public class AddTransactionDialog extends MarvusDialog
{
	private JTextField name, money, date;
	private JComboBox<String> category;
	
	public AddTransactionDialog(BudgetDataTable bdt)
	{
		super("New transaction", bdt);
	}
	
	@Override
	protected JComponent[] createInputs()
	{
		String path = "data/category.txt";
		String[] categoryList = { "Custom" }; 
		try
		{
			FileHelper.checkFile(path);
			List<String> l = Persistance.loadTXT(path);
			categoryList = new String[l.size()];
			int i = 0;
			for (String s : l)
				categoryList[i++] = s;
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(bdt, e);
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
		JComponent[] inputs = {
				new JLabel("Category:"),
				category,
				name,
				new JLabel("Amount:"),
				money,
				new JLabel("Date:"),
				date
		};
		return inputs;
	}

	@Override
	protected Consumer<JComponent[]> consume()
	{
		return con -> {
			bdt.getDataBox().add(new Transaction(name.getText(), money.getText(), date.getText()));
			bdt.refresh();
		};
	}
}
