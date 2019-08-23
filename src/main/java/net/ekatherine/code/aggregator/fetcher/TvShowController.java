package net.ekatherine.code.aggregator.fetcher;

import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.TvShowService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController(value = "fetcherTvShowController")
public class TvShowController extends MainController
{
	private final ExternalSourceAdapter<TvShow> externalSourceAdapter;
	private final TvShowService tvShowService;

	public TvShowController(@Qualifier("tvMazeTvShowAdapter") final ExternalSourceAdapter<TvShow> externalSourceAdapter, final TvShowService tvShowService)
	{
		this.externalSourceAdapter = externalSourceAdapter;
		this.tvShowService = tvShowService;
	}

	@Transactional
	@GetMapping(value = "/tvshow/addBy")
	public TvShow addByTvMazeId(@RequestParam final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final TvShow fetchedTvShow = externalSourceAdapter.getEntity(tvMazeId);
		return tvShowService.save(fetchedTvShow);
	}

	@Transactional
	@GetMapping(value = "/tvshow/{id}/updateBy")
	public TvShow updateByTvMazeId(@PathVariable final Long id, @RequestParam final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final TvShow tvShow = tvShowService.getOne(id);
		final TvShow fetchedTvShow = externalSourceAdapter.getEntity(tvMazeId);
		return tvShowService.update(tvShow, fetchedTvShow);
	}
}
