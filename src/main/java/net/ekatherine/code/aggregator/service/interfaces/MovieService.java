package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.movie.Movie;

public interface MovieService extends GenericEntityService<Movie>, UpdatableEntityService<Movie>, FindAllEntityService<Movie>
{
}
