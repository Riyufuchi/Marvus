package riyufuchi.marvus.app.gui.windows;

import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import riyufuchi.marvus.marvusData.DataBox;
import riyufuchi.marvus.marvusData.DataUtils;
import riyufuchi.marvus.marvusData.Transaction;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;

/**
 * Created On: 18.04.2023<br>
 * Last Edit: 18.04.2023
 * 
 * @author Riyufuchi
 */
public class AddTransactionDialog
{
	private DataBox<Transaction> dataBox;
	JTextField name, money, date;
	
	public AddTransactionDialog(DataBox<Transaction> dataBox)
	{
		this.dataBox = dataBox;
	}
	
	public void showDialog()
	{
		name = FactoryComponent.newTextField("");
		money = FactoryComponent.newTextField("");
		date = FactoryComponent.newTextField(DataUtils.nowDateString());
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
			dataBox.add(new Transaction(name.getText(), money.getText(), date.getText()));
		};
	}

}
