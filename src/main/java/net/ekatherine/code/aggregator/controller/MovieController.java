package net.ekatherine.code.aggregator.controller;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.service.interfaces.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController extends MainController
{
	private final MovieService service;

	@GetMapping(value = "/movie/{id}")
	public Movie getById(@PathVariable final Long id)
	{
		return service.getOne(id);
	}
}
