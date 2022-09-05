package gui;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import helpers.Helper;
import persistance.Persistance;
import utils.FactoryComponent;
import utils.Values;
import workData.Calculations;
import workData.Money;

/**
 * Created On: 20.04.2022
 * Last Edit: 14.07.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public class Counter extends Window
{
	private DataTableForm dtf;
	private JTextField[] textFields;
	private Calculations cals;
	private boolean dataOk;

	public Counter(DataTableForm dtf)
	{
		super("Counter", 100, 200, true, true, false);
		this.dtf = dtf;
		this.dataOk = true;
		this.cals = new Calculations();
	}

	@Override
	protected void setComponents(JPanel content)
	{
		try
		{
			createLabels(content);
		}
		catch (NullPointerException | IOException e)
		{
			new ErrorWindow(ErrorCause.IO, e.getMessage());
		}
	}
	
	private void createTextFilds(JPanel content, int x, String[] actions)
	{
		textFields = new JTextField[x];
		int i;
		for(i = 0; i < textFields.length; i++)
		{
			textFields[i] = new JTextField("0.0");
			textFields[i].setName(actions[i]);
			textFields[i].setFont(Values.FONT_MAIN);
			content.add(textFields[i], getGBC(1, i));
		}
		textFields[i - 1].setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
	}
	
	private void createLabels(JPanel content) throws NullPointerException, IOException
	{
		LinkedList<String> labelTexts = (LinkedList<String>) Persistance.loadFromCSV("data/counter.csv");
		Iterator<String> it = labelTexts.iterator();
		String[] texts;
		String[] actions = new String[labelTexts.size()];
		int i = 0;
		while(it.hasNext())
		{
			texts = it.next().split(";");
			actions[i] = texts[1];
			content.add(FactoryComponent.newLabel(texts[0]), getGBC(0, i));
			i++;
		}
		content.add(FactoryComponent.createButton("Cancel", event -> this.dispose()), getGBC(0, i));
		content.add(FactoryComponent.createButton("Calculate", event -> calculate()), getGBC(1, i));
		createTextFilds(content, i, actions);
	}
	
	private void calculate()
	{
		resetBorders();
		for(int i = 0; i < textFields.length; i++)
		{
			if(textFields[i].getText().isBlank())
			{
				textFields[i].setBorder(new LineBorder(Color.RED));
				dataOk = false;
			}
		}
		final int max = textFields.length - 1;
		if(dataOk && dtf != null)
		{
			try
			{
				for(int x = 0; x < max; x++)
					if(textFields[x].getName().equals("-"))
						cals.add("-" + Helper.checkDoubleFormat(textFields[x].getText()));
					else
						cals.add(Helper.checkDoubleFormat(textFields[x].getText()));
			}
			catch(NullPointerException | IllegalArgumentException e)
			{
				new ErrorWindow(ErrorCause.INERNAL, e.getMessage());
			}
			dtf.getDataBox().add(new Money(cals.getSum().toString(), textFields[max].getText()));
			dtf.refresh();
			this.dispose();
		}
		repaint();
	}
	
	private void resetBorders()
	{
		for(int i = 0; i < 0; i++)
			textFields[i].setBorder(Values.TEXTFIELD_DEFAULT_BORDER);
		repaint();
	}
}
