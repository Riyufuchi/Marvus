package riyufuchi.marvus.app.windows;

import java.io.IOException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.marvusData.Money;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.marvus.marvusLib.utils.DateUtils;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;

/**
 * Dialog for adding new transaction. Also base class for other dialogs regarding transactions.<br><br>
 * 
 * Created On: 16.05.2023<br>
 * Last Edit: 28.06.2023
 * 
 * @author Riyufuchi
 */
public class AddDialog extends SufuDialog
{
	protected JTextField name, money, date, currency;
	protected JComboBox<String> category;
	protected JTextArea note;
	
	public AddDialog(JFrame parentFrame)
	{
		super("New transaction", parentFrame, DialogType.OK);
	}
	
	protected void generateCategoryList(String path)
	{
		try
		{
			FileHelper.checkFile(path);
			SufuPersistence.saveToCSV(path, new String[]{"Custom"});
			DialogHelper.informationDialog(parentFrame, "Generated default " + path, "Category list fixer info");
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(parentFrame, e);
		}
	}

	@Override
	protected void createInputs(JPanel pane)
	{
		String path =  MarvusConfig.workFolder + "category.txt";
		String[] categoryList = { "Custom" }; 
		try
		{
			List<String> l = SufuPersistence.loadTextFile(path);
			categoryList = new String[l.size()];
			int i = 0;
			for (String s : l)
				categoryList[i++] = s;
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(parentFrame, e);
			generateCategoryList(path);
		}
		category = FactoryComponent.<String>createCombobox(categoryList);
		name = FactoryComponent.newTextField("");
		money = FactoryComponent.newTextField("");
		date = FactoryComponent.newTextField(DateUtils.nowDateString());
		currency = FactoryComponent.newTextField(Money.getDefaultCurrency());
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
		note = FactoryComponent.newTextArea("");
		// Set labels
		pane.add(new JLabel("Category:"), getGBC(0, 0));
		pane.add(new JLabel("Amount:"), getGBC(0, 2));
		pane.add(new JLabel("Currency:"), getGBC(0, 3));
		pane.add(new JLabel("Date:"), getGBC(0, 4));
		pane.add(new JLabel("Note:"), getGBC(0, 5));
		// Set components
		pane.add(category, getGBC(1, 0));
		pane.add(name, getGBC(1, 1));
		pane.add(money, getGBC(1, 2));
		pane.add(currency, getGBC(1, 3));
		pane.add(date, getGBC(1, 4));
		pane.add(note, getGBC(1, 5));
	}
	@Override
	protected void onOK()
	{
		((BudgetDataTable)parentFrame).getDataBox().add(new Transaction(name.getText(), money.getText(), date.getText(), note.getText()));
		((BudgetDataTable)parentFrame).refresh();
	}
}
