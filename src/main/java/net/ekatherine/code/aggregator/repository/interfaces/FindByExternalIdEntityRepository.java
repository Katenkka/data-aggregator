package net.ekatherine.code.aggregator.repository.interfaces;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface FindByExternalIdEntityRepository<T>
{
	@Query(value = "select a from #{#entityName} a join a.identifiers meta where (KEY(meta) = :name and meta = :value) ")
	Optional<T> findOneByExternalIdentifier(@Param("name") String name, @Param("value") String value);
}
