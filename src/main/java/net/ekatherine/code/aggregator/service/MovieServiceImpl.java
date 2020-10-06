package net.ekatherine.code.aggregator.service;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.repository.interfaces.MovieRepository;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService
{
	private final MovieRepository movieRepository;
	private final Util util;

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
	public Page<Movie> findAll(Pageable pageable)
	{
		return movieRepository.findAll(pageable);
	}

	@Override
	public Optional<Movie> findByExtIdentifier(final String name, final String value) {
		return movieRepository.findOneByExternalIdentifier(name, value);
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
