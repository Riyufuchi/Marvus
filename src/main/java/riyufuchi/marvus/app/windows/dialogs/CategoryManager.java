package riyufuchi.marvus.app.windows.dialogs;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import riyufuchi.marvus.app.utils.MarvusCategory;
import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.sufuLib.gui.SufuDialog;
import riyufuchi.sufuLib.utils.files.SufuPersistence;
import riyufuchi.sufuLib.utils.gui.SufuDialogHelper;
import riyufuchi.sufuLib.utils.gui.SufuFactory;
import riyufuchi.sufuLib.utils.gui.SufuGuiTools;

public class CategoryManager extends SufuDialog
{
	private JButton sort;
	
	public CategoryManager(JFrame parentFrame)
	{
		super("Category manager", parentFrame, DialogType.OK);
	}

	@Override
	protected void createInputs(JPanel arg0)
	{
		sort = SufuFactory.newButton("Sort", evt -> {
			Arrays.sort(MarvusCategory.categoryList);
			try
			{
				SufuPersistence.saveToCSV(MarvusConfig.workFolder + "category.txt", MarvusCategory.categoryList);
			} 
			catch (NullPointerException | IOException e)
			{
				SufuDialogHelper.exceptionDialog(parentFrame, e);
				return;
			}
			SufuDialogHelper.informationDialog(parentFrame, "Sorted", "Category sorter");
		});
		SufuGuiTools.addLabels(this, "Sort by name:");
		SufuGuiTools.addComponents(this, 1, 0, sort);
	}

	@Override
	protected void onOK()
	{
		
	}

}
