package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.movie.Movie;

import java.util.List;

public interface MovieService extends GenericEntityService<Movie>, UpdatableEntityService<Movie>
{
	List<Movie> findAll();
}
