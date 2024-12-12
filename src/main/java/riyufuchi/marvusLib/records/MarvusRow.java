package riyufuchi.marvusLib.records;

public record MarvusRow<K, E>(K id, E entity)
{
	@Override
	public String toString()
	{
		return entity.toString();
	}
	
}
