package net.ekatherine.code.aggregator.service.interfaces;

public interface ExistingEntityService<T>
{
	public T replaceWithExisting(T obj);
}