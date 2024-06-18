package riyufuchi.marvusLib.interfaces;

import java.util.Optional;

public interface IDatabase<E>
{
	Optional<E> getByID(final int ID);
}
