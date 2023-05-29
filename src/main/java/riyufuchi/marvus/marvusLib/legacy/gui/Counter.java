package riyufuchi.marvus.marvusLib.legacy.gui;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import riyufuchi.marvus.marvusData.MoneySum;
import riyufuchi.marvus.marvusLib.legacy.utils.ComponentParts;
import riyufuchi.marvus.marvusLib.utils.MoneyCalculations;
import riyufuchi.sufuLib.config.SufuLibFonts;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.Persistance;
import riyufuchi.sufuLib.utils.general.InputChecker;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;

/**
 * Created On: 20.04.2022<br>
 * Last Edit: 29.05.2023
 * 
 * @author Riyufuchi
 */
public class Counter extends SufuWindow
{
	private DataTableForm dtf;
	private JTextField[] textFields;
	private MoneyCalculations cals;
	private boolean dataOk;

	public Counter(DataTableForm dtf)
	{
		super("Counter", 100, 200, true, true, false);
		postWindowInit(getPane());
		this.dtf = dtf;
		this.dataOk = true;
		this.cals = new MoneyCalculations();
	}
	
	@Override
	protected void postWindowInit(JPanel content)
	{
		try
		{
			createLabels(this.getPane());
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(this, e);
			this.dispose();
		}
	}

	@Override
	protected void setComponents(JPanel content)
	{
	}
	
	private void createTextFilds(JPanel content, int x, String[] actions)
	{
		textFields = new JTextField[x];
		int i;
		for(i = 0; i < textFields.length; i++)
		{
			textFields[i] = new JTextField("0.0");
			textFields[i].setName(actions[i]);
			textFields[i].setFont(SufuLibFonts.MAIN);
			content.add(textFields[i], getGBC(1, i));
		}
		textFields[i - 1].setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
	}
	
	//TODO: Add default counter.csv
	private void createLabels(JPanel content) throws NullPointerException, IOException
	{
		LinkedList<String> labelTexts = (LinkedList<String>) Persistance.loadTextFile("data/counter.csv");
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
						cals.add("-" + InputChecker.checkDoubleFormat(textFields[x].getText()));
					else
						cals.add(InputChecker.checkDoubleFormat(textFields[x].getText()));
			}
			catch(NullPointerException | IllegalArgumentException e)
			{
				DialogHelper.exceptionDialog(this, e);
			}
			dtf.getDataBox().add(new MoneySum(cals.getSum().toString(), textFields[max].getText()));
			dtf.refresh();
			this.dispose();
		}
		repaint();
	}
	
	private void resetBorders()
	{
		for(int i = 0; i < 0; i++)
			textFields[i].setBorder(ComponentParts.TEXTFIELD_DEFAULT_BORDER);
		repaint();
	}
}
