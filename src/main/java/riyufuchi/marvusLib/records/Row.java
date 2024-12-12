package riyufuchi.marvusLib.records;

public record Row<E>(int id, E entity)
{
	@Override
	public String toString()
	{
		return entity.toString();
	}
	
}
