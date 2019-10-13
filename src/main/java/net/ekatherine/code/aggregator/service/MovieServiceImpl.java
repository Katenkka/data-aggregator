package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.repository.interfaces.MovieRepository;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService
{
	private final MovieRepository movieRepository;
	private final Util util;

	public MovieServiceImpl(final MovieRepository movieRepository, final Util util)
	{
		this.movieRepository = movieRepository;
		this.util = util;
	}

	@Override
	public Movie getOne(final Long id)
	{
		return movieRepository.getOne(id);
	}

	@Override
	public Movie save(final Movie entity)
	{
		return movieRepository.saveAndFlush(entity);
	}

	@Override
	public List<Movie> findAll()
	{
		return movieRepository.findAll();
	}

	@Override
	public Optional<Movie> findByExtIdentifier(final String extId) {
		return findAll().stream()
			.filter(entity -> entity.getIdentifiers().containsKey(Constants.IMDB_ID)
				&& entity.getIdentifiers().get(Constants.IMDB_ID).equalsIgnoreCase(extId))
			.findFirst();
	}

	@Override
	public Movie update(final Movie dest, final Movie src)
	{
		util.consumeSuppliedIfTrue(dest::setDescription, src::getDescription, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setTitle, src::getTitle, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setReleasedAt, src::getReleasedAt, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setStatus, src::getStatus, Objects::nonNull);

		dest.setUpdatedAt(Instant.now());
		return save(dest);
	}
}
