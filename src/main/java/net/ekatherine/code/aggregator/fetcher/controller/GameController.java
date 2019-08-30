package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.GameService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController(value = "fetcherGameController")
public class GameController extends MainController
{
	private final ExternalSourceAdapter<Game> externalSourceAdapter;
	private final GameService service;

	public GameController(@Qualifier("giantBombGameAdapter") final ExternalSourceAdapter<Game> externalSourceAdapter, final GameService service)
	{
		this.externalSourceAdapter = externalSourceAdapter;
		this.service = service;
	}

	@Transactional
	@GetMapping(value = "/game/addBy")
	public Game addByGiantBombGuid(@RequestParam final String giantBombGuid) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Game fetched = externalSourceAdapter.getEntity(giantBombGuid);
		return service.save(fetched);
	}

	@Transactional
	@GetMapping(value = "/game/{id}/updateBy")
	public Game updateByGiantBombGuid(@PathVariable final Long id, @RequestParam final String tvMazeId) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Game existing = service.getOne(id);
		final Game fetched = externalSourceAdapter.getEntity(tvMazeId);
		return service.update(existing, fetched);
	}
}
