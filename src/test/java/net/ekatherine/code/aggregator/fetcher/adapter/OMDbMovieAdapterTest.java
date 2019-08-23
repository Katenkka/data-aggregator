package net.ekatherine.code.aggregator.fetcher.adapter;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.movie.Movie;
import net.ekatherine.code.aggregator.entity.movie.MovieIdentifier;
import net.ekatherine.code.aggregator.fetcher.FetcherUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OMDbMovieAdapterTest
{
	private final String apiKey = "MYSECRETAPIKEY";
	@MockBean
	private FetcherUtil fetcherUtil;
	@SpyBean
	private Util util;
	private OMDbMovieAdapter omDbMovieAdapter;

	@Before
	public void setUp()
	{
		omDbMovieAdapter = new OMDbMovieAdapter(apiKey, fetcherUtil, util);
	}

	@Test
	public void getExistingEntityReturnsMovieFilledWithAllFields() throws IOException
	{
		final String imdbId = "tt2771372";

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("http://www.omdbapi.com/?i=" + imdbId + "&apikey=" + apiKey), Mockito.anyInt())).thenReturn(getSuccessfulFullResponse());

		final Movie movie = omDbMovieAdapter.getEntity(imdbId);

		Assert.assertEquals("Veronica Mars", movie.getTitle());
		Assert.assertEquals("https://m.media-amazon.com/images/M/MV5BMTQ4MDc0Mjg4OV5BMl5BanBnXkFtZTgwODk3NjYyMTE@._V1_SX300.jpg", movie.getPosterUrl());
		Assert.assertEquals("Years after walking away from her past as a young private eye, Veronica Mars gets pulled back to her hometown, just in time for her high school reunion, in order to help her old flame Logan Echolls, who's embroiled in a murder mystery.", movie.getDescription());
		Assert.assertEquals(6.8, movie.getImdbRating(), 0);
		Assert.assertEquals(62, movie.getMetascore(), 0);

		Assert.assertEquals(LocalDateTime.of(2014, 3, 13, 0, 0).toInstant(ZoneOffset.UTC), movie.getReleasedAt());

		Assert.assertEquals(Stream.of("Crime", "Drama", "Mystery", "Thriller").map(Subject::new).collect(Collectors.toSet()), movie.getGenres());

		Assert.assertEquals(Stream.of("Rob Thomas").map(Party::new).collect(Collectors.toSet()), movie.getDirectors());

		Assert.assertEquals(Stream.of("Kristen Bell", "Jason Dohring", "Enrico Colantoni", "Chris Lowell").map(Party::new).collect(Collectors.toSet()), movie.getActors());

		Assert.assertEquals(Stream.of(new String[][]{{"imdb", "tt2771372"},}).map(data -> new MovieIdentifier(movie, data[0], data[1])).collect(Collectors.toSet()), movie.getIdentifiers());
	}

	private String getSuccessfulFullResponse()
	{
		return "{\"Title\":\"Veronica Mars\",\"Year\":\"2014\",\"Rated\":\"PG-13\",\"Released\":\"13 Mar 2014\",\"Runtime\":\"107 min\",\"Genre\":\"Crime, Drama, Mystery, Thriller\",\"Director\":\"Rob Thomas\",\"Writer\":\"Rob Thomas (screenplay), Diane Ruggiero-Wright (screenplay), Rob Thomas (story), Rob Thomas (characters)\",\"Actors\":\"Kristen Bell, Jason Dohring, Enrico Colantoni, Chris Lowell\",\"Plot\":\"Years after walking away from her past as a young private eye, Veronica Mars gets pulled back to her hometown, just in time for her high school reunion, in order to help her old flame Logan Echolls, who's embroiled in a murder mystery.\",\"Language\":\"English\",\"Country\":\"USA, UK, France, Germany\",\"Awards\":\"6 nominations.\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMTQ4MDc0Mjg4OV5BMl5BanBnXkFtZTgwODk3NjYyMTE@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"6.8/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"79%\"},{\"Source\":\"Metacritic\",\"Value\":\"62/100\"}],\"Metascore\":\"62\",\"imdbRating\":\"6.8\",\"imdbVotes\":\"47,253\",\"imdbID\":\"tt2771372\",\"Type\":\"movie\",\"DVD\":\"06 May 2014\",\"BoxOffice\":\"$2,664,765\",\"Production\":\"Warner Bros.\",\"Website\":\"http://theveronicamarsmovie.com/\",\"Response\":\"True\"}\n";
	}
}