package net.ekatherine.code.aggregator.repository.interfaces;

import java.util.List;

interface FindByTitleEntityRepository<T>
{
	List<T> findByTitleIgnoreCase(String title);
}
