package riyufuchi.marvus.app.windows;

import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.Persistance;
import riyufuchi.sufuLib.utils.gui.FactoryComponent;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 24.05.2023
 * 
 * @author Riyufuchi
 */
public class SettingsDialog extends SufuDialog
{
	private JComboBox<AppThemeUI> themes;
	private JComboBox<String> dateFormat;
	
	public SettingsDialog(JFrame parentFrame)
	{
		super("Preferences", parentFrame, DialogType.YesCancel);
	}

	@Override
	protected void createInputs(JPanel content)
	{
		SufuWindowTools.createLabels(this, "Theme: ", "Date format: ");
		themes = FactoryComponent.<AppThemeUI>createCombobox(AppThemeUI.values());
		themes.setSelectedIndex(SufuLibConfig.themeID.ordinal());
		content.add(themes, getGBC(1,0));
		dateFormat = FactoryComponent.<String>createCombobox(AppTexts.DATE_FORMAT_OPTIONS);
		content.add(dateFormat, getGBC(1,1));
	}

	@Override
	protected void onOK()
	{
		try
		{
			Persistance.saveToCSV(FileHelper.checkFile(MarvusConfig.SETTINGS_FILE_PATH).getPath(),
					themes.getItemAt(themes.getSelectedIndex()).toString(),
					String.valueOf(dateFormat.getSelectedIndex()));
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		DialogHelper.informationDialog(parentFrame, "The application requires a restart for the changes to take effect.", "Applying settings");
		CustomizeUI.setUI(themes.getItemAt(themes.getSelectedIndex()));
		CustomizeUI.refreshTheme(parentFrame);
		parentFrame.pack();
	}
}
