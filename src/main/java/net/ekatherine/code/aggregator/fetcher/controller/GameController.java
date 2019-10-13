package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.GameService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController(value = "fetcherGameController")
public class GameController extends MainController
{
	private final ExternalSourceAdapter<Game> externalSourceAdapter;
	private final GameService gameService;

	public GameController(@Qualifier("giantBombGameAdapter") final ExternalSourceAdapter<Game> externalSourceAdapter, final GameService gameService)
	{
		this.externalSourceAdapter = externalSourceAdapter;
		this.gameService = gameService;
	}

	@Transactional
	@GetMapping(value = "/game/addBy")
	public Game addByGiantBombGuid(@RequestParam final String giantBombGuid) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Game fetched = externalSourceAdapter.getEntity(giantBombGuid);
		return gameService.save(fetched);
	}

	@Transactional
	@GetMapping(value = "/game/{id}/updateBy")
	public Game updateByGiantBombGuid(@PathVariable final Long id, @RequestParam final String giantBombGuid) throws IOException, NoEntityFromExternalSourceFoundException {
		final Game existing = gameService.getOne(id);
		return updateGameByExternalId(existing, giantBombGuid);
	}

	@Transactional
	@GetMapping(value = "/game/updateAllByGiantBombGuid")
	public void updateAllByGiantBombGuid()
	{
		final List<Game> all = gameService.findAll();
		for (final Game game : all) {
			try {
				final String giantBombGuid = game.getIdentifiers().get(Constants.GIANT_BOMB_ID);
				updateGameByExternalId(game, giantBombGuid);
			} catch (final Throwable e) {
				LoggerFactory.getLogger(getClass()).debug("Something went wrong while updating Game with id = {}", game.getId());
				LoggerFactory.getLogger(getClass()).error("Message: ", e);
			}
		}
	}

	private Game updateGameByExternalId(final Game game, final String giantBombGuid) throws IOException, NoEntityFromExternalSourceFoundException {
		final Game fetched = externalSourceAdapter.getEntity(giantBombGuid);
		return gameService.update(game, fetched);
	}
}
