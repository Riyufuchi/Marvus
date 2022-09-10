package gui.windows;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import general.utils.Values;
import gui.info.AppTexts;
import gui.utils.FactoryComponent;

/**
 * Created On: 14.07.2022
 * Last Edit: 10.09.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public final class Settings extends Window
{
	private JComboBox<String> themes, dateFormat, backgroundColor;
	
	public Settings()
	{
		super("Preferences", 330, 210, true, true, false);
		createOptions(getPane());
	}

	@Override
	protected void setComponents(JPanel content)
	{
		String[] labels = {"Theme: ", "Date format: ", "Background:"};
		createLabels(labels);
		
		content.add(FactoryComponent.createButton("Cancel", event -> this.dispose()), getGBC(0, labels.length));
		content.add(FactoryComponent.createButton("Ok", event -> {
			applyPreferences();
			this.dispose();
		}), getGBC(1, labels.length));
	}
	
	private void createOptions(JPanel panel)
	{
		themes = FactoryComponent.<String>createCombobox(AppTexts.THEMES);
		themes.setSelectedIndex(Values.themeID);
		panel.add(themes, getGBC(1,0));
		
		dateFormat = FactoryComponent.<String>createCombobox(AppTexts.DATE_FORMAT_OPTIONS);
		panel.add(dateFormat, getGBC(1,1));
		
		backgroundColor = FactoryComponent.<String>createCombobox(AppTexts.COLOR_OPTIONS);
		panel.add(backgroundColor, getGBC(1,2));
	}
	
	/**
	 * TODO: add functionality save preferences into config file and load it when application start 
	 */
	private void applyPreferences()
	{
		//Helper.setUI(themes.getSelectedIndex());
	}
}
