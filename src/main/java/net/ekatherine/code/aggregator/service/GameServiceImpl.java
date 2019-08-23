package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.repository.interfaces.GameRepository;
import net.ekatherine.code.aggregator.service.interfaces.GameService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GameServiceImpl implements GameService
{
	private final GameRepository repository;
	private final Util util;

	public GameServiceImpl(final GameRepository gameRepository, final Util util)
	{
		repository = gameRepository;
		this.util = util;
	}

	@Override
	public Game getOne(final Long id)
	{
		return repository.getOne(id);
	}

	@Override
	public Game save(final Game entity)
	{
		return repository.saveAndFlush(entity);
	}

	@Override
	public Game update(final Game dest, final Game src)
	{
		util.consumeSuppliedIfTrue(dest::setDescription, src::getDescription, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setTitle, src::getTitle, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setAliases, src::getAliases, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setReleasedAt, src::getReleasedAt, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setStatus, src::getStatus, Objects::nonNull);

		return repository.saveAndFlush(dest);
	}
}
