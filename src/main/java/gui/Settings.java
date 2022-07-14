package gui;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import utils.FactoryComponent;
import utils.Helper;

/**
 * Created On: 14.07.2022
 * Last Edit: 14.07.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public final class Settings extends Window
{
	private JComboBox<String> themes, dateFormat;
	
	public Settings()
	{
		super("Preferences", 320, 260, true, false, false);
		createOptions(getPane());
	}

	@Override
	protected void setComponents(JPanel content)
	{
		String[] labels = {"Theme: ", "Date format: "};
		createLabels(labels);
		
		content.add(FactoryComponent.createButton("Cancel", event -> this.dispose()), getGBC(0, labels.length));
		content.add(FactoryComponent.createButton("Ok", event -> {
			applyPreferences();
			this.dispose();
		}), getGBC(1, labels.length));
	}
	
	private void createOptions(JPanel panel)
	{
		String[] themesOptions = {"Default", "System matching", "Nimbus"};
		themes = FactoryComponent.<String>createCombobox();
		for (int l = 0; l < themesOptions.length; l++)
		{
			themes.addItem(themesOptions[l]);
		}
		panel.add(themes, getGBC(1,0));
		
		String[] dateFormatOptions = {"dd/mm/yyyy", "mm/dd/yyyy"};
		dateFormat = FactoryComponent.<String>createCombobox();
		for (int l = 0; l < dateFormatOptions.length; l++)
		{
			dateFormat.addItem(dateFormatOptions[l]);
		}
		panel.add(dateFormat, getGBC(1,1));
	}
	
	/**
	 * TODO: add functionality save preferences into config file and load it when application start 
	 */
	private void applyPreferences()
	{
		Helper.setUI(themes.getSelectedIndex());
	}
}
