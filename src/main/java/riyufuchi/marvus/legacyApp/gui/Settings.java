package riyufuchi.marvus.legacyApp.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.gui.SufuWindow;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuFactory;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 29.08.2023
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
		content.add(SufuFactory.createButton("Cancel", event -> this.dispose()), getGBC(0, labels.length));
		content.add(SufuFactory.createButton("Ok", event -> {
			try
			{
				applyPreferences();
			}
			catch (NullPointerException | IOException e)
			{
				SufuDialogHelper.exceptionDialog(this, e);
			}
			this.dispose();
		}), getGBC(1, labels.length));
		themes = SufuFactory.<AppThemeUI>createCombobox(AppThemeUI.values());
		themes.setSelectedIndex(SufuLibConfig.themeID.ordinal());
		content.add(themes, getGBC(1,0));
		
		dateFormat = SufuFactory.<String>createCombobox(AppTexts.DATE_FORMAT_OPTIONS);
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
		File config = SufuFileHelper.checkFile("data/config.csv");
		String[] data = { themes.getItemAt(themes.getSelectedIndex()).toString(), String.valueOf(dateFormat.getSelectedIndex())};
		SufuPersistence.saveToCSV(config.getPath(), data);
	}
}
