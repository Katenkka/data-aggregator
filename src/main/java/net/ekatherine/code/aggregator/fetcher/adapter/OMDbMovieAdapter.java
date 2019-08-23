package net.ekatherine.code.aggregator.fetcher.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.entity.movie.MovieIdentifier;
import net.ekatherine.code.aggregator.fetcher.FetcherUtil;
import net.ekatherine.code.aggregator.fetcher.adapter.helper.ParsedEntity;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Component("omdbMovieAdapter")
public class OMDbMovieAdapter implements ExternalSourceAdapter<Movie>
{
	private final String apiKey;
	private final FetcherUtil fetcherUtil;
	private final Util util;

	public OMDbMovieAdapter(@Value("${omdb.apiKey}") final String apiKey, final FetcherUtil fetcherUtil, final Util util)
	{
		this.apiKey = apiKey;
		this.fetcherUtil = fetcherUtil;
		this.util = util;
	}

	@Override
	public Movie getEntity(@NotBlank final String imdbId) throws IOException
	{
		final Entity parsedEntity = (Entity) fetchExternalEntity(imdbId);

		return populateExternalContentToEntity(parsedEntity);
	}

	private ParsedEntity fetchExternalEntity(final String key) throws IOException
	{
		final String json = fetcherUtil.connectAndRetrieveJson("http://www.omdbapi.com/?i=" + key + "&apikey=" + apiKey, 10000);

		final Type type = new TypeToken<Entity>()
		{
		}.getType();

		return new Gson().fromJson(json, type);
	}

	private Movie populateExternalContentToEntity(final ParsedEntity parsedEntity)
	{
		final Movie movie = new Movie();
		final Entity parsedMovie = (Entity) parsedEntity;

		movie.setTitle(util.sanitize(parsedMovie.Title));
		movie.setDescription(util.sanitize(parsedMovie.Plot));
		movie.setPosterUrl(util.sanitize(parsedMovie.Poster));
		movie.setMetascore(parsedMovie.Metascore);
		movie.setImdbRating(parsedMovie.imdbRating);

		if (Objects.nonNull(parsedMovie.imdbID))
		{
			final MovieIdentifier movieIdentifier = new MovieIdentifier();
			movieIdentifier.setType("imdb");
			movieIdentifier.setValue(util.sanitize(parsedMovie.imdbID));
			movieIdentifier.setEntity(movie);
			movie.setIdentifiers(Collections.singleton(movieIdentifier));
		}

		if (Objects.nonNull(parsedMovie.Released))
		{
			final LocalDate localDate = LocalDate.parse(parsedMovie.Released, DateTimeFormatter.ofPattern("d MMM uuuu"));
			final Instant releasedDate = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
			movie.setReleasedAt(releasedDate);
		}

		Optional.ofNullable(parsedMovie.Genre)
			.map(str -> Arrays.asList(str.split(",")))
			.orElseGet(Collections::emptyList)
			.forEach(subj -> movie.addGenre(new Subject(util.sanitize(subj))));

		Optional.ofNullable(parsedMovie.Director)
			.map(str -> Arrays.asList(str.split(",")))
			.orElseGet(Collections::emptyList)
			.forEach(subj -> movie.addDirector(new Party(util.sanitize(subj))));

		Optional.ofNullable(parsedMovie.Actors)
			.map(str -> Arrays.asList(str.split(",")))
			.orElseGet(Collections::emptyList)
			.forEach(subj -> movie.addActor(new Party(util.sanitize(subj))));

		return movie;
	}

	private static class Entity implements ParsedEntity
	{
		String Title;
		String Released;
		String Genre;
		String Director;
		String Actors;
		String Plot;
		String Poster;
		Double Metascore;
		Double imdbRating;
		String imdbID;
	}
}
