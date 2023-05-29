package riyufuchi.marvus.marvusLib.legacy.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 29.05.2023
 * 
 * @author Riyufuchi
 */
public final class Settings extends SufuWindow
{
	private JComboBox<AppThemeUI> themes;
	private JComboBox<String> dateFormat;
	
	public Settings()
	{
		super("Preferences", 360, 210, true, true, false);
	}

	@Override
	protected void setComponents(JPanel content)
	{
		String[] labels = {"Theme: ", "Date format: "};
		createLabels(labels);
		content.add(FactoryComponent.createButton("Cancel", event -> this.dispose()), getGBC(0, labels.length));
		content.add(FactoryComponent.createButton("Ok", event -> {
			try
			{
				applyPreferences();
			}
			catch (NullPointerException | IOException e)
			{
				DialogHelper.exceptionDialog(this, e);
			}
			this.dispose();
		}), getGBC(1, labels.length));
		themes = FactoryComponent.<AppThemeUI>createCombobox(AppThemeUI.values());
		themes.setSelectedIndex(SufuLibConfig.themeID.ordinal());
		content.add(themes, getGBC(1,0));
		
		dateFormat = FactoryComponent.<String>createCombobox(AppTexts.DATE_FORMAT_OPTIONS);
		content.add(dateFormat, getGBC(1,1));
	}
	
	/**
	 * Saves chosen settings options
	 * 
	 * @throws IOException 
	 * @throws NullPointerException 
	 */
	private void applyPreferences() throws NullPointerException, IOException
	{
		File config = FileHelper.checkFile("data/config.csv");
		String[] data = { themes.getItemAt(themes.getSelectedIndex()).toString(), String.valueOf(dateFormat.getSelectedIndex())};
		SufuPersistence.saveToCSV(config.getPath(), data);
	}
}
