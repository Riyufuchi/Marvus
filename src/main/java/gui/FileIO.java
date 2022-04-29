package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import persistance.FilesIO;
import persistance.XML;
import utils.Values;
import workData.Money;

/**
 * Created On: 11.04.2022
 * Last Edit: 11.04.2022
 * @author riyufuchi
 *
 */
@SuppressWarnings("serial")
public class FileIO extends Window
{

	private JButton cancel, ok;
	private JLabel[] labels;
	private JComboBox<String>[] comboBoxes;
	private JTextField fileName, pathToFile;
	private LinkedList<Money> list;
	private DataTableForm dtf;
	private final boolean export;
	
	public FileIO(boolean export)
	{
		super("FileIO", 225, 175, true, true, false);
		this.export = export;
		this.dtf = null;
		if(export)
			ok.setText("Export data");
		setTitle(getTitle() + " - " + ok.getText());
	}

	@Override
	protected void setComponents(JPanel content)
	{
		createComboBoxes(content);
		createTextFields();
		createButtons();
		createLabels(content);
		createEvents();
		content.add(fileName, getGBC(1, 2));
		content.add(pathToFile, getGBC(1, 3));
		content.add(ok, getGBC(1, 4));
		content.add(cancel, getGBC( 0, 4));
	}
	
	@SuppressWarnings("unchecked")
	private void createComboBoxes(JPanel content)
	{
		comboBoxes = new JComboBox[2];
		for (int i = 0; i < comboBoxes.length; i++)
		{
			comboBoxes[i] = new JComboBox<>();
			comboBoxes[i].setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
			comboBoxes[i].setFont(Values.FONT_MAIN);
			for (int l = 0; l < Values.FIO_COMBO_BOX_ITEMS[i].length; l++)
			{
				comboBoxes[i].addItem(Values.FIO_COMBO_BOX_ITEMS[i][l]);
			}
			content.add(comboBoxes[i], getGBC(1, i));
		}
	}
	
	private void createButtons()
	{
		ok = new JButton("Import data");
		cancel = new JButton("Cancel");
		ok.setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
		ok.setFont(Values.FONT_MAIN);
		cancel.setBackground(Values.DEFAULT_BUTTON_BACKGROUND);
		cancel.setFont(Values.FONT_MAIN);
	}
	
	private void createTextFields()
	{
		fileName = new JTextField();
		fileName.setFont(Values.FONT_MAIN);
		fileName.setText("data");
		pathToFile = new JTextField();
		pathToFile.setText("data/");
		pathToFile.setEnabled(false);
		pathToFile.setFont(Values.FONT_MAIN);
	}
	
	private void createLabels(JPanel content)
	{
		labels = new JLabel[Values.FIO_LABELS.length];
		for(int i = 0; i < labels.length; i++)
		{
			labels[i] = new JLabel();
			labels[i].setText(Values.FIO_LABELS[i]);
			content.add(labels[i], getGBC( 0, i));
		}
	}
	
	private void createEvents()
	{
		comboBoxes[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(comboBoxes[1].getSelectedIndex() == 1)
					pathToFile.setEnabled(true);
				else
				{
					pathToFile.setEnabled(false);
					pathToFile.setText("data/");
				}
			}
			
		});
		ok.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(export)
					exportNow();
				else
					try 
					{
						importNow();
					}catch(NullPointerException | IllegalArgumentException e1)
					{
						new ErrorWindow("NullPtr", e1.getMessage());
					}
				safelyClose();
			}
		});
		cancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent evt) 
			{
				safelyClose();
			}
		});
	}
	
	private void safelyClose()
	{
		this.dispose();
	}

	private void exportNow()
	{
		if (!(fileName.getText().isBlank() && !list.isEmpty()))
		{
			switch (comboBoxes[0].getSelectedIndex())
			{
				case 0 -> { FilesIO.saveToCVS(getPath(), list); }
				case 1 -> { new XML(getPath(), "MoneyExport", "Money").exportXML(list); }
				case 2 -> { FilesIO.writeBinary(getPath(), list); }
			}
		}
	}

	private void importNow() throws NullPointerException, IllegalArgumentException
	{
		//list = new LinkedList<>();
		if(dtf == null)
			throw new NullPointerException("No destinaton for data import is set");
		if (!(pathToFile.getText()).equals(""))
		{
			switch (comboBoxes[0].getSelectedIndex())
			{
				case 0 -> {
					//list = FilesIO.loadFromCVS(getPath());
					dtf.loadData(FilesIO.loadFromCVS(getPath()));
				}
				case 1 -> {
					XML xml = new XML(getPath(), "MoneyExport", "Money");
					xml.parsujMoney();
					dtf.loadData(xml.getList());
				}
				case 2 -> {
					dtf.loadData(FilesIO.loadBinary(getPath()));
				}
			}
		}
	}
	
	//SETTERS
	
	public void setList(LinkedList<Money> list) throws NullPointerException
	{
		if(list == null)
			throw new NullPointerException();
		this.list = list;
	}
	
	public void setImportDestination(DataTableForm dtf) throws NullPointerException
	{
		if(dtf == null)
			throw new NullPointerException();
		this.dtf = dtf;
	}

	//GETTERS
	
	public String getPath()
	{
		switch(comboBoxes[1].getSelectedIndex())
		{
			case 0 -> { return "data/" + fileName.getText() + comboBoxes[0].getSelectedItem(); }
			case 1 -> { return pathToFile.getText() + fileName.getText() + comboBoxes[0].getSelectedItem();}
		}
		return "";
	}
}
