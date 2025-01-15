package riyufuchi.marvus.interfaces;

import riyufuchi.marvusLib.interfaces.MarvusDatabaseController;

/**
 * 
 * @author Riyufuchi
 * @since 25.12.2023
 * @version 15.01.2025
 */
public interface IMarvusController
{
	// Setters
	void setDatabase(MarvusDatabaseController md);
	// Getters
	MarvusDatabaseController getDatabase();
}
