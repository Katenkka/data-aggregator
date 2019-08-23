package net.ekatherine.code.aggregator.fetcher.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.entity.game.GameIdentifier;
import net.ekatherine.code.aggregator.fetcher.FetcherUtil;
import net.ekatherine.code.aggregator.fetcher.adapter.helper.ParsedEntity;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class GiantBombGameAdapter implements ExternalSourceAdapter<Game>
{
	private final String apiKey;
	private final FetcherUtil fetcherUtil;
	private final Util util;

	public GiantBombGameAdapter(@Value("${giantBomb.apiKey}") final String apiKey, final FetcherUtil fetcherUtil, final Util util)
	{
		this.apiKey = apiKey;
		this.fetcherUtil = fetcherUtil;
		this.util = util;
	}

	@Override
	public Game getEntity(@NotBlank final String giantBombGuid) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Entity parsedGame = (Entity) fetchExternalEntity(giantBombGuid);

		final Game game = populateExternalContentToEntity(parsedGame);

		final GameIdentifier identifier = new GameIdentifier();
		identifier.setType("giantBombGuid");
		identifier.setValue(giantBombGuid);
		identifier.setEntity(game);
		game.addIdentifier(identifier);

		return game;
	}

	private ParsedEntity fetchExternalEntity(final String giantBombKey) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final String json = fetcherUtil.connectAndRetrieveJson("https://www.giantbomb.com/api/game/" + giantBombKey + "/?api_key=" + apiKey + "&format=json&field_list=publishers,themes,name,deck,aliases,concepts,developers,original_release_date,expected_release_day,expected_release_month,expected_release_year,genres,image", 10000);

		final Type type = new TypeToken<Response>()
		{
		}.getType();

		final Response container = new Gson().fromJson(json, type);

		if (Objects.isNull(container) || Objects.isNull(container.results))
		{
			throw new NoEntityFromExternalSourceFoundException();
		}

		return container.results;
	}

	private Game populateExternalContentToEntity(final ParsedEntity parsedEntity)
	{
		final Game game = new Game();
		final Entity parsedGame = (Entity) parsedEntity;

		game.setTitle(util.sanitize(parsedGame.name));
		game.setAliases(util.sanitize(parsedGame.aliases));
		game.setDescription(util.sanitize(parsedGame.deck));

		if (Objects.nonNull(parsedGame.image) && Objects.nonNull(parsedGame.image.original_url))
		{
			game.setPosterUrl(util.sanitize(parsedGame.image.original_url));
		}

		if (Objects.nonNull(parsedGame.expected_release_day) && Objects.nonNull(parsedGame.expected_release_month) && Objects.nonNull(parsedGame.expected_release_year))
		{
			final LocalDate localDate = LocalDate.of(parsedGame.expected_release_year, parsedGame.expected_release_month, parsedGame.expected_release_day);
			final Instant releasedDate = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
			game.setReleasedAt(releasedDate);

			//@todo check if it's still used...
			final String original_release_date = parsedGame.original_release_date;
		}

		Optional.ofNullable(parsedGame.developers)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.map(party -> new Party(util.sanitize(party.name)))
			.forEach(game::addDeveloper);

		Optional.ofNullable(parsedGame.publishers)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.map(party -> new Party(util.sanitize(party.name)))
			.forEach(game::addPublisher);

		Optional.ofNullable(parsedGame.genres)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.map(subject -> new Subject(util.sanitize(subject.name)))
			.forEach(game::addGenre);

		Optional.ofNullable(parsedGame.themes)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.map(subject -> new Subject(util.sanitize(subject.name)))
			.forEach(game::addTheme);

		Optional.ofNullable(parsedGame.concepts)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.map(subject -> new Subject(util.sanitize(subject.name)))
			.forEach(game::addConcept);

		return game;
	}

	private static class Response
	{
		Entity results;
	}

	private static class Entity implements ParsedEntity
	{
		String aliases;
		String deck;
		Integer expected_release_day;
		Integer expected_release_month;
		Integer expected_release_year;
		Image image;
		String name;
		String original_release_date;
		Nameable[] concepts;
		Nameable[] developers;
		Nameable[] publishers;
		Nameable[] genres;
		Nameable[] themes;
	}

	private static class Image
	{
		String original_url;
	}

	private static class Nameable
	{
		String name;
	}
}
