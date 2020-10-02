package net.ekatherine.code.aggregator.fetcher.controller;

import lombok.extern.slf4j.Slf4j;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.TvShowService;
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

@RestController(value = "fetcherTvShowController")
@Slf4j
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
		return tvShowService.mergeWithExisting(fetchedTvShow);
	}

	@Transactional
	@GetMapping(value = "/tvshow/{id}/updateBy")
	public TvShow updateByTvMazeId(@PathVariable final Long id, @RequestParam final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException {
		final TvShow tvShow = tvShowService.getOne(id);
		return updateTvShowByTvMazeId(tvShow, tvMazeId);
	}

	@GetMapping(value = "/tvshow/updateAllByTvMazeId")
	@ResponseStatus(HttpStatus.OK)
	public void updateAllByTvMazeId(@PageableDefault(size = 200)
									@SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.ASC)})
									Pageable pageable)
	{
		Page<TvShow> page = tvShowService.findAll(pageable);
		if(page.isEmpty()) {
			return;
		}

		page.get().forEach(tvShow -> {
			try {
				final String showTvMazeId = tvShow.getIdentifiers().get(Constants.TV_MAZE_ID);
				updateTvShowByTvMazeId(tvShow, showTvMazeId);
			} catch (final Throwable e) {
				log.debug("Something went wrong while updating TVShow with id = {}", tvShow.getId());
				log.error("Message: ", e);
			}
		});
	}

	private TvShow updateTvShowByTvMazeId(final TvShow tvShow, final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException {
		final TvShow fetchedTvShow = externalSourceAdapter.getEntity(tvMazeId);
		return tvShowService.update(tvShow, fetchedTvShow);
	}
}
