package riyufuchi.marvus.interfaces;

import riyufuchi.marvus.database.MarvusDatabase;

/**
 * 
 * @author Riyufuchi
 * @since 25.12.2023
 * @version 25.12.2023
 */
public interface IMarvusController
{
	// Setters
	void setDatabase(MarvusDatabase md);
	// Getters
	MarvusDatabase getDatabase();
}
