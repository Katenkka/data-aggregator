package net.ekatherine.code.aggregator.entity.interfaces;

import java.util.Collection;

public interface Series<T>
{
	Collection<T> getEntities();

	void setEntities(Collection<T> entities);
}
