package net.ekatherine.code.aggregator.service;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.repository.interfaces.TvShowRepository;
import net.ekatherine.code.aggregator.service.interfaces.TvShowService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TvShowServiceImpl implements TvShowService
{
	private final TvShowRepository tvShowRepository;
	private final Util util;

	@Override
	public TvShow getOne(final Long id)
	{
		return tvShowRepository.getOne(id);
	}

	@Override
	public List<TvShow> findAll()
	{
		return tvShowRepository.findAll();
	}

	@Override
	public Optional<TvShow> findByExtIdentifier(final String extId) {
		return findAll().stream()
			.filter(show -> show.getIdentifiers().containsKey(Constants.TV_MAZE_ID)
				&& show.getIdentifiers().get(Constants.TV_MAZE_ID).equalsIgnoreCase(extId))
			.findFirst();
	}

	@Override
	public TvShow save(final TvShow entity)
	{
		return tvShowRepository.saveAndFlush(entity);
	}

	@Override
	public TvShow update(final TvShow dest, final TvShow src)
	{
		util.consumeSuppliedIfTrue(dest::setDescription, src::getDescription, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setTitle, src::getTitle, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setPremieredAt, src::getPremieredAt, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setStatus, src::getStatus, Objects::nonNull);

		dest.setUpdatedAt(Instant.now());

		dest.updateEpisodes(src.getEpisodes());

		return save(dest);
	}
}
