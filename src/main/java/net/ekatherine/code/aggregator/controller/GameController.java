package net.ekatherine.code.aggregator.controller;

import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.service.interfaces.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController extends MainController
{
	private final GameService service;

	public GameController(final GameService service)
	{
		this.service = service;
	}

	@GetMapping(value = "/game/{id}")
	public Game getById(@PathVariable final Long id)
	{
		return service.getOne(id);
	}
}
