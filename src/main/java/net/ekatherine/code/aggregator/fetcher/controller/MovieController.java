package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
	public Movie addByIsbn(@RequestParam final String imdbId) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Movie fetchedMovie = externalSourceAdapter.getEntity(imdbId);
		return movieService.save(fetchedMovie);
	}
}
