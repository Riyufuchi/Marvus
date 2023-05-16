package riyufuchi.marvus.app.windows;

import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.DialogHelper;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.gui.utils.FactoryComponent;
import riyufuchi.sufuLib.gui.utils.SufuWindowTools;
import riyufuchi.sufuLib.utils.files.FileHelper;
import riyufuchi.sufuLib.utils.files.Persistance;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 16.05.2023
 * 
 * @author Riyufuchi
 */
public class SettingsDialog extends SufuDialog
{
	private JComboBox<AppThemeUI> themes;
	private JComboBox<String> dateFormat;
	private final String settingsFile = "data/config.txt";
	
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
			Persistance.saveToCSV(FileHelper.checkFile(settingsFile).getPath(),
					themes.getItemAt(themes.getSelectedIndex()).toString(),
					String.valueOf(dateFormat.getSelectedIndex()));
		}
		catch (NullPointerException | IOException e)
		{
			DialogHelper.exceptionDialog(parentFrame, e);
		}
	}

}
