package net.ekatherine.code.aggregator.fetcher.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.fetcher.adapter.helper.ParsedEntity;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.util.FetcherUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TvMazeTvShowAdapter implements ExternalSourceAdapter<TvShow>
{
	private final FetcherUtil fetcherUtil;
	private final Util util;
	private final TvMazeEpisodesAdapter tvMazeEpisodesAdapter;

	@Override
	public TvShow getEntity(@NotBlank final String tvMazeId) throws IOException
	{
		final Entity parsedEntity = (Entity) fetchExternalEntity(tvMazeId);

		final TvShow tvShow = populateExternalContentToEntity(parsedEntity);

		tvShow.addIdentifier(Constants.TV_MAZE_ID, tvMazeId);

		tvShow.setEpisodes(tvMazeEpisodesAdapter.getEntities(tvMazeId, tvShow));

		return tvShow;
	}

	private ParsedEntity fetchExternalEntity(final String key) throws IOException
	{
		final String json = fetcherUtil.connectAndRetrieveJson("http://api.tvmaze.com/shows/" + key + "?embed=cast", 10000);

		final Type type = new TypeToken<Entity>()
		{
		}.getType();

		return new Gson().fromJson(json, type);
	}

	private TvShow populateExternalContentToEntity(final ParsedEntity parsedEntity)
	{
		final TvShow tvShow = new TvShow();
		final Entity parsedTvShow = (Entity) parsedEntity;

		tvShow.setTitle(util.sanitize(parsedTvShow.name));
		tvShow.setDescription(util.sanitize(parsedTvShow.summary));
		tvShow.setStatus(StatusEnum.getStatusByInternalCode(parsedTvShow.status));

		if (Objects.nonNull(parsedTvShow.premiered))
		{
			tvShow.setPremieredAt(Instant.parse(parsedTvShow.premiered + "T00:00:00.00Z"));
		}

		if (Objects.nonNull(parsedTvShow.image) && Objects.nonNull(parsedTvShow.image.original))
		{
			tvShow.setPosterUrl(util.sanitize(parsedTvShow.image.original));
		}

		Optional.ofNullable(parsedTvShow.genres)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.forEach(obj -> tvShow.addGenre(new Subject(util.sanitize(obj))));

		if (!StringUtils.isEmpty(parsedTvShow.externals.imdb))
		{
			tvShow.addIdentifier(Constants.IMDB_ID, util.sanitize(parsedTvShow.externals.imdb));
		}

		if (!StringUtils.isEmpty(parsedTvShow.externals.thetvdb))
		{
			tvShow.addIdentifier(Constants.THE_TV_DB_ID, util.sanitize(parsedTvShow.externals.thetvdb));
		}

		if (!StringUtils.isEmpty(parsedTvShow.externals.tvrage))
		{
			tvShow.addIdentifier(Constants.TV_RAGE_ID, util.sanitize(parsedTvShow.externals.tvrage));
		}

		if (Objects.nonNull(parsedTvShow._embedded) && Objects.nonNull(parsedTvShow._embedded.cast))
		{
			Arrays.stream(parsedTvShow._embedded.cast).filter(
				cast -> Objects.nonNull(cast) && Objects.nonNull(cast.person) && Objects.nonNull(cast.person.name)
			).forEach(obj -> tvShow.addActor(new Party(util.sanitize(obj.person.name))));
		}

		return tvShow;
	}

	private static class Entity implements ParsedEntity
	{
		String name;
		String summary;
		String status;
		String premiered;
		String[] genres;
		Identifier externals;
		Image image;
		Embedded _embedded;
	}

	private static class Embedded
	{
		Cast[] cast;
	}

	private static class Cast
	{
		Person person;
	}

	private static class Person
	{
		String name;
	}

	private static class Image
	{
		String original;
	}

	private static class Identifier
	{
		String tvrage;
		String thetvdb;
		String imdb;
	}
}
