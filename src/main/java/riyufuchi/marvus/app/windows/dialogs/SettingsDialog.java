package riyufuchi.marvus.app.windows.dialogs;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.AppTexts;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.marvus.app.utils.MarvusUtils;
import riyufuchi.marvus.app.windows.MarvusDataWindow;
import riyufuchi.sufuLib.config.CustomizeUI;
import riyufuchi.sufuLib.config.SufuLibConfig;
import riyufuchi.sufuLib.enums.AppThemeUI;
import riyufuchi.sufuLib.gui.SufuDialogHelper;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.SufuFileHelper;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiInputTools;
import riyufuchi.sufuLib.utils.gui.SufuWindowTools;

/**
 * Created On: 14.07.2022<br>
 * Last Edit: 15.09.2023
 * 
 * @author Riyufuchi
 */
public class SettingsDialog extends SufuDialog
{
	private JComboBox<AppThemeUI> themes;
	private JComboBox<String> dateFormat, windowSize;
	private JButton workFile;
	private JCheckBox showQuitDialog;
	
	public SettingsDialog(JFrame parentFrame)
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
		SufuWindowTools.createLabels(this, "Window size:", "Theme:", "Date format:", "Work file:", "Show quit dialog: ");
		themes = SufuFactory.<AppThemeUI>newCombobox(AppThemeUI.values());
		windowSize = SufuFactory.<String>newCombobox(AppTexts.WINDOW_SIZE);
		dateFormat = SufuFactory.<String>newCombobox(AppTexts.DATE_FORMAT_OPTIONS);
		workFile = SufuFactory.newButton("None", evt -> currentWorkFileBtnEvent());
		showQuitDialog = new JCheckBox();
		selectConfig();
		int y = 0;
		for (JComponent comp : new JComponent[]{ windowSize, themes, dateFormat, workFile, showQuitDialog}) // TODO: Make this into SufuWindowTools
			content.add(comp, getGBC(1, y++));
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
					SufuGuiInputTools.extractComboboxValue(windowSize),
					SufuGuiInputTools.<AppThemeUI>extractComboboxValue(themes).toString(),
					String.valueOf(dateFormat.getSelectedIndex()),
					path,
					String.valueOf(showQuitDialog.isSelected()));
		}
		catch (NullPointerException | IOException e)
		{
			SufuDialogHelper.exceptionDialog(parentFrame, e);
			return;
		}
		if (!SufuGuiInputTools.<AppThemeUI>extractComboboxValue(themes).equals(MarvusConfig.appTheme) ||
			!SufuGuiInputTools.<String>extractComboboxValue(windowSize).equals(MarvusConfig.width + "x" + MarvusConfig.height))
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
