package riyufuchi.marvus.dialogs.tools.other;

import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.MarvusTexts;
import riyufuchi.marvus.app.MarvusDataWindow;
import riyufuchi.marvus.utils.MarvusConfig;
import riyufuchi.marvus.utils.MarvusGuiUtils;
import riyufuchi.sufuLib.config.SufuCustomUI;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.SufuAppTheme;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuComponentTools;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

/**
 * @author Riyufuchi
 * @since 14.07.2022
 * @version 12.09.2024
 */
public class PreferencesDialog extends SufuDialog
{
	private JComboBox<SufuAppTheme> themes;
	private JComboBox<String> dateFormat, windowSize;
	private JButton workFile;
	private JCheckBox showQuitDialog;
	private JCheckBox autoLoadCheck;
	private JCheckBox autoMaximizeCheck;
	
	public PreferencesDialog(JFrame parentFrame)
	{
		super("Preferences", parentFrame, DialogType.YesCancel, true, true);
		pack();
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
		if (MarvusConfig.defaultWorkFile != null)
			workFile.setText(MarvusConfig.defaultWorkFile.getName());
		showQuitDialog.setSelected(MarvusConfig.showQuitDialog);
		autoLoadCheck.setSelected(MarvusConfig.autoLoadData);
		autoMaximizeCheck.setSelected(MarvusConfig.autoMaximize);
	}
	
	@Override
	protected void createInputs(JPanel content)
	{
		themes = SufuFactory.<SufuAppTheme>newCombobox(SufuAppTheme.values());
		windowSize = SufuFactory.<String>newCombobox(MarvusTexts.WINDOW_SIZE);
		dateFormat = SufuFactory.<String>newCombobox(MarvusTexts.DATE_FORMAT_OPTIONS);
		workFile = SufuFactory.newButton("None", evt -> currentWorkFileBtnEvent());
		showQuitDialog = new JCheckBox();
		autoLoadCheck = new JCheckBox();
		autoMaximizeCheck = new JCheckBox();
		selectConfig();
		// Column 1
		SufuGuiTools.addLabels(this, "Window size:", "Theme:", "Date format:", "Work file:");
		SufuGuiTools.addComponents(this, 1, 0, windowSize, themes, dateFormat, workFile);
		// Column 2
		SufuGuiTools.addLabels(this, 2, "Show quit dialog: ", "Auto load data: ", "Auto maximize:");
		SufuGuiTools.addComponents(this, 3, 0, showQuitDialog, autoLoadCheck, autoMaximizeCheck);
	}

	@Override
	protected void onOK()
	{
		String path = "None";
		if (MarvusConfig.defaultWorkFile != null)
			path = MarvusConfig.defaultWorkFile.getAbsolutePath();
		try
		{
			SufuPersistence.saveToCSV(SufuFileHelper.checkFile(MarvusConfig.SETTINGS_FILE_PATH).getPath(),
					SufuComponentTools.extractComboboxValue(windowSize),
					SufuComponentTools.<SufuAppTheme>extractComboboxValue(themes).toString(),
					String.valueOf(dateFormat.getSelectedIndex()),
					path,
					String.valueOf(showQuitDialog.isSelected()),
					String.valueOf(autoLoadCheck.isSelected()),
					String.valueOf(autoMaximizeCheck.isSelected()));
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		if (!SufuComponentTools.<SufuAppTheme>extractComboboxValue(themes).equals(MarvusConfig.appTheme) ||
			!SufuComponentTools.<String>extractComboboxValue(windowSize).equals(MarvusConfig.width + "x" + MarvusConfig.height))
		{
			SufuDialogHelper.informationDialog(parentFrame, "The application requires a restart for the changes to take effect.", "Applying settings");
			SufuCustomUI.setTheme(themes.getItemAt(themes.getSelectedIndex()));
			SufuCustomUI.refreshTheme(parentFrame);
			parentFrame.pack();
		}
	}
	
	private void currentWorkFileBtnEvent()
	{
		File currentWorkFile =  MarvusGuiUtils.createTransactionIO(((MarvusDataWindow)parentFrame)).showLoadChooserLoadAndGetFile();
		if (currentWorkFile != null)
		{
			workFile.setText(currentWorkFile.getName());
			MarvusConfig.defaultWorkFile = currentWorkFile;
		}
	}
}
