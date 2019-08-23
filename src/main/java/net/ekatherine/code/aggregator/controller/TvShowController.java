package net.ekatherine.code.aggregator.controller;

import net.ekatherine.code.aggregator.entity.tv.Episode;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.service.interfaces.TvShowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TvShowController extends MainController
{
	private final TvShowService tvShowService;

	public TvShowController(final TvShowService tvShowService)
	{
		this.tvShowService = tvShowService;
	}

	@GetMapping(value = "/tvshow/{id}")
	public TvShow getById(@PathVariable final Long id)
	{
		return tvShowService.getOne(id);
	}

	@GetMapping(value = "/tvshow/{id}/latest")
	public Episode getLatestByTvShowId(@PathVariable final Long id)
	{
		return tvShowService.getOne(id).getLatestEpisode();
	}
}
