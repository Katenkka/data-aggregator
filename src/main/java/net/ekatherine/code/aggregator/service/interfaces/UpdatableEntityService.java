package net.ekatherine.code.aggregator.service.interfaces;

interface UpdatableEntityService<T>
{
	T update(T dest, T src);
}
