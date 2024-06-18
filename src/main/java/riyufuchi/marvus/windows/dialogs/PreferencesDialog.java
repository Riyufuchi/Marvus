package riyufuchi.marvus.windows.dialogs;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.utils.AppTexts;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusUtils;
import riyufuchi.marvus.windows.MarvusDataWindow;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.AppTheme;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 25.09.2023
 * 
 * @author Riyufuchi
 */
public class PreferencesDialog extends SufuDialog
{
	private JComboBox<AppTheme> themes;
	private JComboBox<String> dateFormat, windowSize;
	private JButton workFile;
	private JCheckBox showQuitDialog;
	
	public PreferencesDialog(JFrame parentFrame)
	{
		super("Preferences", parentFrame, DialogType.YesCancel);
	}

	private void selectConfig()
	{
		themes.setSelectedIndex(SufuLibConfig.themeID.ordinal());
		if (MarvusConfig.fullscreen)
			windowSize.setSelectedIndex(0);
		else
		{
			windowSize.setSelectedItem(MarvusConfig.width + "x" + MarvusConfig.height);
		}
		if (MarvusConfig.currentWorkFile != null)
			workFile.setText(MarvusConfig.currentWorkFile.getName());
		showQuitDialog.setSelected(MarvusConfig.showQuitDialog);
	}
	
	@Override
	protected void createInputs(JPanel content)
	{
		SufuGuiTools.addLabels(this, "Window size:", "Theme:", "Date format:", "Work file:", "Show quit dialog: ");
		themes = SufuFactory.<AppTheme>newCombobox(AppTheme.values());
		windowSize = SufuFactory.<String>newCombobox(AppTexts.WINDOW_SIZE);
		dateFormat = SufuFactory.<String>newCombobox(AppTexts.DATE_FORMAT_OPTIONS);
		workFile = SufuFactory.newButton("None", evt -> currentWorkFileBtnEvent());
		showQuitDialog = new JCheckBox();
		selectConfig();
		SufuGuiTools.addComponents(this, 1, 0, windowSize, themes, dateFormat, workFile, showQuitDialog);
	}

	@Override
	protected void onOK()
	{
		String path = "None";
		if (MarvusConfig.currentWorkFile != null)
			path = MarvusConfig.currentWorkFile.getAbsolutePath();
		try
		{
			SufuPersistence.saveToCSV(SufuFileHelper.checkFile(MarvusConfig.SETTINGS_FILE_PATH).getPath(),
					SufuComponentTools.extractComboboxValue(windowSize),
					SufuComponentTools.<AppTheme>extractComboboxValue(themes).toString(),
					String.valueOf(dateFormat.getSelectedIndex()),
					path,
					String.valueOf(showQuitDialog.isSelected()));
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		if (!SufuComponentTools.<AppTheme>extractComboboxValue(themes).equals(MarvusConfig.appTheme) ||
			!SufuComponentTools.<String>extractComboboxValue(windowSize).equals(MarvusConfig.width + "x" + MarvusConfig.height))
		{
			SufuDialogHelper.informationDialog(parentFrame, "The application requires a restart for the changes to take effect.", "Applying settings");
			CustomizeUI.setUI(themes.getItemAt(themes.getSelectedIndex()));
			CustomizeUI.refreshTheme(parentFrame);
			parentFrame.pack();
		}
	}
	
	private void currentWorkFileBtnEvent()
	{
		MarvusConfig.currentWorkFile =  MarvusUtils.createTransactionIO(((MarvusDataWindow)parentFrame)).showLoadChooser();
		if (MarvusConfig.currentWorkFile != null)
			workFile.setText(MarvusConfig.currentWorkFile.getName());
	}
}
