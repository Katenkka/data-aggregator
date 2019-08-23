package net.ekatherine.code.aggregator.service.interfaces;

public interface GenericEntityService<T>
{
	T getOne(Long id);

	T save(T entity);
}
