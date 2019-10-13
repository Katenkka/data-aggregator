package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController(value = "fetcherMovieController")
public class MovieController extends MainController
{
	private final ExternalSourceAdapter<Movie> externalSourceAdapter;
	private final MovieService movieService;

	public MovieController(@Qualifier("omdbMovieAdapter") final ExternalSourceAdapter<Movie> externalSourceAdapter, final MovieService movieService)
	{
		this.externalSourceAdapter = externalSourceAdapter;
		this.movieService = movieService;
	}

	@Transactional
	@GetMapping(value = "/movie/addBy")
	public Movie updateOrAddNewByImdbId(@RequestParam final String imdbId) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Movie fetchedMovie = externalSourceAdapter.getEntity(imdbId);
		final Optional<Movie> existing = movieService.findByExtIdentifier(imdbId);
		if (existing.isPresent()) {
			return movieService.update(existing.get(), fetchedMovie);
		}
		return movieService.save(fetchedMovie);
	}

	@Transactional
	@GetMapping(value = "/movie/{id}/updateBy")
	public Movie updateByImdbId(@PathVariable final Long id, @RequestParam final String imdbId) throws IOException, NoEntityFromExternalSourceFoundException {
		final Movie movie = movieService.getOne(id);
		return updateMovieByExternalId(movie, imdbId);
	}

	@Transactional
	@GetMapping(value = "/movie/updateAllByImdbId")
	public void updateAllByImdbId()
	{
		final List<Movie> all = movieService.findAll();
		for (final Movie movie : all) {
			try {
				final String imdbKey = movie.getIdentifiers().get(Constants.IMDB_ID);
				updateMovieByExternalId(movie, imdbKey);
			} catch (final Throwable e) {
				LoggerFactory.getLogger(getClass()).debug("Something went wrong while updating Movie with id = {}", movie.getId());
				LoggerFactory.getLogger(getClass()).error("Message: ", e);
			}
		}
	}

	private Movie updateMovieByExternalId(final Movie movie, final String imdbKey) throws IOException, NoEntityFromExternalSourceFoundException {
		final Movie fetchedMovie = externalSourceAdapter.getEntity(imdbKey);
		return movieService.update(movie, fetchedMovie);
	}
}