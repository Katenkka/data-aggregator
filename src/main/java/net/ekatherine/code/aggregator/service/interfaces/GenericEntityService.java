package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.interfaces.HasExternalIdentifiers;

import java.util.Map;
import java.util.Optional;

interface GenericEntityService<T extends HasExternalIdentifiers> extends UpdatableEntityService<T>, FindAllEntityService<T>
{
	T getOne(Long id);

	T save(T entity);

	Optional<T> findByExtIdentifier(String name, String value);

	default T mergeWithExisting(T fetched)
	{
		for (Map.Entry<String, String> entry : fetched.getIdentifiers().entrySet()) {
			final Optional<T> existing = findByExtIdentifier(entry.getKey(), entry.getValue());
			if (existing.isPresent()) {
				return update(existing.get(), fetched);
			}
		}

		return save(fetched);
	}
}
