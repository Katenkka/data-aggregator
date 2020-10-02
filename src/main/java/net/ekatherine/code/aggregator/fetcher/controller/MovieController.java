package net.ekatherine.code.aggregator.fetcher.controller;

import lombok.extern.slf4j.Slf4j;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController(value = "fetcherMovieController")
@Slf4j
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
		return movieService.mergeWithExisting(fetchedMovie);
	}

	@Transactional
	@GetMapping(value = "/movie/{id}/updateBy")
	public Movie updateByImdbId(@PathVariable final Long id, @RequestParam final String imdbId) throws IOException, NoEntityFromExternalSourceFoundException {
		final Movie movie = movieService.getOne(id);
		return updateMovieByExternalId(movie, imdbId);
	}

	@GetMapping(value = "/movie/updateAllByImdbId")
	@ResponseStatus(HttpStatus.OK)
	public void updateAllByImdbId(@PageableDefault(size = 200)
								  @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.ASC)})
								  Pageable pageable)
	{
		Page<Movie> page = movieService.findAll(pageable);
		if(page.isEmpty()) {
			return;
		}

		page.get().forEach(movie -> {
			try {
				final String imdbKey = movie.getIdentifiers().get(Constants.IMDB_ID);
				updateMovieByExternalId(movie, imdbKey);
			} catch (final Throwable e) {
				log.debug("Something went wrong while updating Movie with id = {}", movie.getId());
				log.error("Message: ", e);
			}
		});
	}

	private Movie updateMovieByExternalId(final Movie movie, final String imdbKey) throws IOException, NoEntityFromExternalSourceFoundException {
		final Movie fetchedMovie = externalSourceAdapter.getEntity(imdbKey);
		return movieService.update(movie, fetchedMovie);
	}
}