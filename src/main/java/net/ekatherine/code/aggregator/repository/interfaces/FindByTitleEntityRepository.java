package net.ekatherine.code.aggregator.repository.interfaces;

import java.util.List;

public interface FindByTitleEntityRepository<T>
{
	List<T> findByTitleIgnoreCase(String title);
}
