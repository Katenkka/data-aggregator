package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.TvShowService;
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
	public TvShow updateOrAddNewByTvMazeId(@RequestParam final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final TvShow fetchedTvShow = externalSourceAdapter.getEntity(tvMazeId);
		final Optional<TvShow> existingTvShow = tvShowService.findByExtIdentifier(tvMazeId);
		if (existingTvShow.isPresent()) {
			return tvShowService.update(existingTvShow.get(), fetchedTvShow);
		}
		return tvShowService.save(fetchedTvShow);
	}

	@Transactional
	@GetMapping(value = "/tvshow/{id}/updateBy")
	public TvShow updateByTvMazeId(@PathVariable final Long id, @RequestParam final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException {
		final TvShow tvShow = tvShowService.getOne(id);
		return updateTvShowByTvMazeId(tvShow, tvMazeId);
	}

	@Transactional
	@GetMapping(value = "/tvshow/updateAllByTvMazeId")
	public void updateAllByTvMazeId()
	{
		final List<TvShow> all = tvShowService.findAll();
		for (final TvShow tvShow : all) {
			try {
				final String showTvMazeId = tvShow.getIdentifiers().get(Constants.TV_MAZE_ID);
				updateTvShowByTvMazeId(tvShow, showTvMazeId);
			} catch (final Throwable e) {
				LoggerFactory.getLogger(getClass()).debug("Something went wrong while updating TVShow with id = {}", tvShow.getId());
				LoggerFactory.getLogger(getClass()).error("Message: ", e);
			}
		}
	}

	private TvShow updateTvShowByTvMazeId(final TvShow tvShow, final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException {
		final TvShow fetchedTvShow = externalSourceAdapter.getEntity(tvMazeId);
		return tvShowService.update(tvShow, fetchedTvShow);
	}
}
