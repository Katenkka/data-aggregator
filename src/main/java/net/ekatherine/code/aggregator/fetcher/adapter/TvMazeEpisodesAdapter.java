package net.ekatherine.code.aggregator.fetcher.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.tv.Episode;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.fetcher.adapter.helper.ParsedEntity;
import net.ekatherine.code.aggregator.fetcher.util.FetcherUtil;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class TvMazeEpisodesAdapter
{
	private final FetcherUtil fetcherUtil;
	private final Util util;

	TvMazeEpisodesAdapter(final FetcherUtil fetcherUtil, final Util util)
	{
		this.fetcherUtil = fetcherUtil;
		this.util = util;
	}

	List<Episode> getEntities(@NotBlank final String tvMazeId, final TvShow tvShow) throws IOException
	{
		final Set<TvMazeEpisode> tvMazeEpisodeSet = fetchEpisodesByTvShowId(tvMazeId);

		final List<Episode> episodes = populateExternalContentToEntity(tvMazeEpisodeSet, tvShow);

		return episodes.stream().sorted(Episode::comparator).collect(Collectors.toList());
	}

	private Set<TvMazeEpisode> fetchEpisodesByTvShowId(final String key) throws IOException
	{
		final String json = fetcherUtil.connectAndRetrieveJson("http://api.tvmaze.com/shows/" + key + "/episodes", 10000);

		final Type type =
			new TypeToken<Set<TvMazeEpisode>>()
			{
			}.getType();

		return new Gson().fromJson(json, type);
	}

	private List<Episode> populateExternalContentToEntity(final Set<TvMazeEpisode> tvMazeEpisodes, final TvShow tvShow)
	{
		final List<Episode> episodes = new ArrayList<>();

		tvMazeEpisodes.forEach(parsedEpisode ->
		{
			final Episode episode = new Episode();

			episode.setTvShow(tvShow);
			episode.setTitle(util.sanitize(parsedEpisode.name));
			episode.setDescription(util.sanitize(parsedEpisode.summary));

			if (Objects.nonNull(parsedEpisode.airstamp))
			{
				final LocalDateTime dateTime = LocalDateTime.parse(parsedEpisode.airstamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
				episode.setAiredAt(dateTime.toInstant(ZoneOffset.UTC));
			}

			episode.setSeason(parsedEpisode.season);
			episode.setNumber(parsedEpisode.number);

			if (Objects.nonNull(parsedEpisode.image) && Objects.nonNull(parsedEpisode.image.original))
			{
				episode.setPosterUrl(util.sanitize(parsedEpisode.image.original));
			}

			episodes.add(episode);
		});

		return episodes;
	}

	private static class TvMazeEpisode implements ParsedEntity
	{
		String name;
		Long season;
		Long number;
		String airstamp;
		String summary;
		Image image;
	}

	private static class Image
	{
		String original;
	}
}
