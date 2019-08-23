package net.ekatherine.code.aggregator.service.interfaces;

public interface UpdatableEntityService<T>
{
	T update(T dest, T src);
}
