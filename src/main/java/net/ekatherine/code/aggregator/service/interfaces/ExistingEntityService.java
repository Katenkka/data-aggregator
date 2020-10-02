package net.ekatherine.code.aggregator.service.interfaces;

interface ExistingEntityService<T>
{
	T replaceWithExisting(T obj);
}