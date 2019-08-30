package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.repository.interfaces.TvShowRepository;
import net.ekatherine.code.aggregator.service.interfaces.TvShowService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TvShowServiceImpl implements TvShowService
{
	private final TvShowRepository tvShowRepository;
	private final Util util;

	public TvShowServiceImpl(final TvShowRepository tvShowRepository, final Util util)
	{
		this.tvShowRepository = tvShowRepository;
		this.util = util;
	}

	@Override
	public TvShow getOne(final Long id)
	{
		return tvShowRepository.getOne(id);
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

		dest.updateEpisodes(src.getEpisodes());

		return save(dest);
	}
}
