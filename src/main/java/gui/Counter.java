package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import persistance.Persistance;
import utils.Helper;
import utils.Values;
import workData.Calculations;
import workData.DataBox;
import workData.Money;

/**
 * Created On: 20.04.2022
 * Last Edit: 20.04.2022
 * @author riyufuchi
 *
 */
@SuppressWarnings("serial")
public class Counter extends Window
{
	private JButton ok, cancel;
	private DataBox dataBox;
	private JLabel[] labels;
	private JTextField[] textFields;
	private Calculations cals;
	private boolean dataOk;

	public Counter(DataBox dataBox)
	{
		super("Counter", 100, 200, true, true, false);
		this.dataBox = dataBox;
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
			new ErrorWindow("IOError", e.getMessage());
		}
		createTextFilds(content);
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
		if(dataOk)
		{
			try
			{
					for(int x = 0; x < max; x++)
						if(labels[x].getName().equals("-"))
							cals.add("-" + Helper.checkDoubleFormat(textFields[x].getText()));
						else
							cals.add(Helper.checkDoubleFormat(textFields[x].getText()));
			}
			catch(NullPointerException | IllegalArgumentException e)
			{
				new ErrorWindow(Error.INERNAL.toString(), e.getMessage());
			}
			dataBox.add(new Money(cals.getSum().toString(), textFields[max].getText()));
		}
		repaint();
	}
	
	private void resetBorders()
	{
		for(int i = 0; i < 0; i++)
		{
			Helper.makeBorder(textFields[i], Helper.defaultTextFieldBorder());
		}
		repaint();
	}
	
	private void createTextFilds(JPanel content)
	{
		if(labels == null)
			return;
		textFields = new JTextField[labels.length];
		int i;
		for(i = 0; i < textFields.length; i++)
		{
			textFields[i] = new JTextField();
			textFields[i].setFont(Values.FONT_MAIN);
			content.add(textFields[i], getGBC(1, i));
		}
		textFields[i - 1].setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
	}
	
	private void createLabels(JPanel content) throws NullPointerException, IOException
	{
		createButtons();
		LinkedList<String> labelTexts = (LinkedList<String>) Persistance.loadFromCVS("data/counter.cvs");
		Iterator<String> it = labelTexts.iterator();
		String[] texts;
		labels = new JLabel[labelTexts.size()];
		int i;
		for(i = 0; i < labels.length; i++)
		{
			texts = it.next().split(";");
			labels[i] = new JLabel();
			labels[i].setText(texts[0]);
			labels[i].setName(texts[1]);
			labels[i].setFont(Values.FONT_MAIN);
			content.add(labels[i], getGBC(0, i));
		}
		//i++;
		content.add(cancel, getGBC(0, i));
		content.add(ok, getGBC(1, i));
	}
	
	private void createButtons()
	{
		ok = new JButton("Ok");
		cancel = new JButton("Cancel");
		ok.setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
		ok.setFont(Values.FONT_MAIN);
		ActionListener al = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				calculate();
				
			}
		};
		ok.addActionListener(al);
		cancel.setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
		cancel.setFont(Values.FONT_MAIN);
	}

}
