package net.ekatherine.code.aggregator.service;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.repository.interfaces.GameRepository;
import net.ekatherine.code.aggregator.service.interfaces.GameService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService
{
	private final GameRepository gameRepository;
	private final Util util;

	@Override
	public Game getOne(final Long id)
	{
		return gameRepository.getOne(id);
	}

	@Override
	public List<Game> findAll()
	{
		return gameRepository.findAll();
	}

	@Override
	public Game save(final Game entity)
	{
		return gameRepository.saveAndFlush(entity);
	}

	@Override
	public Optional<Game> findByExtIdentifier(final String extId) {
		return findAll().stream()
			.filter(entity -> entity.getIdentifiers().containsKey(Constants.GIANT_BOMB_ID)
				&& entity.getIdentifiers().get(Constants.GIANT_BOMB_ID).equalsIgnoreCase(extId))
			.findFirst();
	}

	@Override
	public Game update(final Game dest, final Game src)
	{
		util.consumeSuppliedIfTrue(dest::setDescription, src::getDescription, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setTitle, src::getTitle, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setAliases, src::getAliases, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setReleasedAt, src::getReleasedAt, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setStatus, src::getStatus, Objects::nonNull);

		dest.setUpdatedAt(Instant.now());
		return save(dest);
	}
}
