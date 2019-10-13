package net.ekatherine.code.aggregator.service.interfaces;

import java.util.Optional;

public interface GenericEntityService<T>
{
	T getOne(Long id);

	T save(T entity);

	Optional<T> findByExtIdentifier(String extId);
}
