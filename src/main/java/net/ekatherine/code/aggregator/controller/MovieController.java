package net.ekatherine.code.aggregator.controller;

import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController extends MainController
{
	private final MovieService service;

	public MovieController(final MovieService service)
	{
		this.service = service;
	}

	@GetMapping(value = "/movie/{id}")
	public Movie getById(@PathVariable final Long id)
	{
		return service.getOne(id);
	}
}
