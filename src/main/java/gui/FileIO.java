package gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import persistance.FilesIO;
import persistance.XML;
import utils.FactoryComponent;
import utils.Values;

/**
 * Created On: 11.04.2022
 * Last Edit: 04.09.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public class FileIO extends Window
{
	private JButton cancel, ok;
	private JComboBox<String>[] comboBoxes;
	private JTextField fileName, pathToFile;
	private DataTableForm dtf;
	private final boolean export;
	
	public FileIO(boolean export, DataTableForm dtf)
	{
		super("FileIO", 260, 210, true, true, false);
		this.export = export;
		this.dtf = dtf;
		if(export)
			ok.setText("Export data");
		createEvents();
		setTitle(getTitle() + " - " + ok.getText());
	}

	@Override
	protected void setComponents(JPanel content)
	{
		createComboBoxes(content);
		createTextFields();
		createLabels(Values.FIO_LABELS);
		ok = FactoryComponent.createButton("Import data", null);
		cancel = FactoryComponent.createButton("Cancel", null);
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
			comboBoxes[i] = FactoryComponent.<String>createCombobox(Values.FIO_COMBO_BOX_ITEMS[i]);
			/*for (int l = 0; l < Values.FIO_COMBO_BOX_ITEMS[i].length; l++)
				comboBoxes[i].addItem(Values.FIO_COMBO_BOX_ITEMS[i][l]);*/
			content.add(comboBoxes[i], getGBC(1, i));
		}
	}
	
	private void createTextFields()
	{
		fileName = FactoryComponent.newTextField("data");
		pathToFile = FactoryComponent.newTextField("data/");
		pathToFile.setEnabled(false);
	}
	
	private void createEvents()
	{
		comboBoxes[1].addActionListener(event -> {
			if(comboBoxes[1].getSelectedIndex() == 1)
				pathToFile.setEnabled(true);
			else
			{
				pathToFile.setEnabled(false);
				pathToFile.setText("data/");
			}
		});
		if(export)
		{
			ok.addActionListener(event -> { exportNow(); safelyClose(); });
		}
		else
		{
			ok.addActionListener(event -> { 
				try 
				{
					importNow();
				}
				catch(NullPointerException | IllegalArgumentException e1)
				{
					new ErrorWindow(ErrorCause.INERNAL, e1.getMessage());
				}
				safelyClose();
			});
		}
		cancel.addActionListener(event -> safelyClose());
	}
	
	private void safelyClose()
	{
		this.dispose();
	}

	private void exportNow()
	{
		if (!(fileName.getText().isBlank() && !dtf.getDataBox().isEmpty()))
		{
			switch (comboBoxes[0].getSelectedIndex())
			{
				case 0 -> { FilesIO.saveToCVS(getPath(), dtf.getDataBox().getList()); }
				case 1 -> { new XML(getPath(), "MoneyExport", "Money").exportXML(dtf.getDataBox().getList()); }
				case 2 -> { FilesIO.writeBinary(getPath(), dtf.getDataBox().getList()); }
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
					//dtf.set(FilesIO.loadFromCVS(getPath()));
					dtf.getDataBox().setList(FilesIO.loadFromCVS(getPath()));
					dtf.refresh();
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
	
	//GETTERS
	
	private String getPath()
	{
		switch(comboBoxes[1].getSelectedIndex())
		{
			case 0 -> { return "data/" + fileName.getText() + comboBoxes[0].getSelectedItem(); }
			case 1 -> { return pathToFile.getText() + fileName.getText() + comboBoxes[0].getSelectedItem();}
			default -> { return ""; }
		}
	}
}
