package gui.windows;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import general.helpers.Helper;
import general.persistance.Persistance;
import general.utils.Values;
import gui.info.AppTexts;
import gui.utils.DialogHelper;
import gui.utils.FactoryComponent;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 07.10.2022
 * 
 * @author Riyufuchi
 */
@SuppressWarnings("serial")
public final class Settings extends Window
{
	private JComboBox<String> themes, dateFormat, backgroundColor;
	
	public Settings()
	{
		super("Preferences", 360, 210, true, true, false);
		createOptions(getPane());
	}

	@Override
	protected void setComponents(JPanel content)
	{
		String[] labels = {"Theme: ", "Date format: ", "Background:"};
		createLabels(labels);
		
		content.add(FactoryComponent.createButton("Cancel", event -> this.dispose()), getGBC(0, labels.length));
		content.add(FactoryComponent.createButton("Ok", event -> {
			try
			{
				applyPreferences();
			} catch (NullPointerException | IOException e)
			{
				DialogHelper.exceptionDialog(this, e);
			}
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
	 * Saves chosen settings options
	 * 
	 * @throws IOException 
	 * @throws NullPointerException 
	 */
	private void applyPreferences() throws NullPointerException, IOException
	{
		File config = Helper.checkFileExistance("config.csv");
		String[] data = { String.valueOf(themes.getSelectedIndex()), String.valueOf(dateFormat.getSelectedIndex()), String.valueOf(backgroundColor.getSelectedIndex()) };
		Persistance.saveToCSV(config.getPath(), data);
	}
}
