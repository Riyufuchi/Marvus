package riyufuchi.marvus.app.gui.windows;

import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import riyufuchi.marvus.marvusData.DateUtils;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 19.04.2023
 * 
 * @author Riyufuchi
 */
public class AddTransactionDialog
{
	private BudgetDataTable bdt;
	private JTextField name, money, date;
	
	public AddTransactionDialog(BudgetDataTable bdt)
	{
		this.bdt = bdt;
	}
	
	public void showDialog()
	{
		name = FactoryComponent.newTextField("");
		money = FactoryComponent.newTextField("");
		date = FactoryComponent.newTextField(DateUtils.nowDateString());
		JComponent[] inputs = {
				new JLabel("Name:"),
				name,
				new JLabel("Amount:"),
				money,
				new JLabel("Date:"),
				date
		};
		DialogHelper.customDialog(null, "Add", inputs, consume());
	}
	
	private Consumer<JComponent[]> consume()
	{
		return con -> {
			bdt.getDataBox().add(new Transaction(name.getText(), money.getText(), date.getText()));
			bdt.refresh();
		};
	}

}
