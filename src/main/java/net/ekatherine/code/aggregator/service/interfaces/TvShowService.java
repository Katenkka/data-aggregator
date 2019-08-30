package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.tv.TvShow;

import java.util.List;

public interface TvShowService extends GenericEntityService<TvShow>, UpdatableEntityService<TvShow>
{
	List<TvShow> findAll();
}