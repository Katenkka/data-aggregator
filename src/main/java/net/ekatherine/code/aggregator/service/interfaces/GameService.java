package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.game.Game;

import java.util.List;

public interface GameService extends GenericEntityService<Game>, UpdatableEntityService<Game>
{
	List<Game> findAll();
}