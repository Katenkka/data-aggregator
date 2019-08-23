package net.ekatherine.code.aggregator.fetcher.adapter;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
import net.ekatherine.code.aggregator.entity.tv.TvShowIdentifier;
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
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TvMazeTvShowAdapterTest
{
	@MockBean
	private FetcherUtil fetcherUtil;
	@SpyBean
	private Util util;

	@MockBean
	private TvMazeEpisodesAdapter tvMazeEpisodesAdapter;

	private TvMazeTvShowAdapter tvMazeTvShowAdapter;

	@Before
	public void setUp() throws IOException
	{
		Mockito.when(tvMazeEpisodesAdapter.getEntities(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyList());
		tvMazeTvShowAdapter = new TvMazeTvShowAdapter(fetcherUtil, util, tvMazeEpisodesAdapter);
	}

	@Test
	public void getExistingEntityReturnsTvShowFilledWithAllFieldsExceptEpisodes() throws IOException
	{
		final String tvMazeId = "473";

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("http://api.tvmaze.com/shows/" + tvMazeId + "?embed=cast"), Mockito.anyInt())).thenReturn(getSuccessfulFullResponse());

		final TvShow tvShow = tvMazeTvShowAdapter.getEntity(tvMazeId);

		Assert.assertEquals("Veronica Mars", tvShow.getTitle());
		Assert.assertEquals("In the wealthy, seaside community of Neptune, the rich and powerful make the rules. They own the town and are desperately trying to keep their dirty little secrets just that... secret. Unfortunately for them, there's Veronica Mars, a smart, fearless private investigator dedicated to solving the town's toughest mysteries. During the day, Veronica studies hard, but by night, she helps her father Keith with his private investigator business, sneaking through back alleys and scoping out no-tell motels with a telescopic-lens camera in an attempt to uncover the California beach town's darkest secrets.", tvShow.getDescription());
		Assert.assertEquals("http://static.tvmaze.com/uploads/images/original_untouched/204/512460.jpg", tvShow.getPosterUrl());

		Assert.assertEquals(Stream.of("Drama", "Crime", "Mystery").map(Subject::new).collect(Collectors.toSet()), tvShow.getGenres());

		Assert.assertEquals(StatusEnum.TBD, tvShow.getStatus());

		Assert.assertEquals(LocalDateTime.of(2004, 9, 22, 0, 0).toInstant(ZoneOffset.UTC), tvShow.getPremieredAt());

		Assert.assertEquals(Stream.of(new String[][]{
			{"tvrage", "6507"},
			{"thetvdb", "73730"},
			{"imdb", "tt0412253"},
			{"tvmaze", tvMazeId},
		}).map(data -> new TvShowIdentifier(tvShow, data[0], data[1])).collect(Collectors.toSet()), tvShow.getIdentifiers());

		Assert.assertTrue(tvShow.getActors().toString(), tvShow.getActors().containsAll(Stream.of("Kristen Bell", "Jason Dohring", "Enrico Colantoni", "Percy Daggs III", "Chris Lowell", "Francis Capra", "Ryan Hansen", "Michael Muhney", "Teddy Dunn", "Tina Majorino", "Julie Gonzalo").map(Party::new).collect(Collectors.toSet())));

		Assert.assertNull(tvShow.getLatestEpisode());
	}

	private String getSuccessfulFullResponse()
	{
		return "{\"id\":473,\"url\":\"http://www.tvmaze.com/shows/473/veronica-mars\",\"name\":\"Veronica Mars\",\"type\":\"Scripted\",\"language\":\"English\",\"genres\":[\"Drama\",\"Crime\",\"Mystery\"],\"status\":\"To Be Determined\",\"runtime\":60,\"premiered\":\"2004-09-22\",\"officialSite\":\"https://www.hulu.com/series/veronica-mars-4626972c-9da7-40fe-aed7-977f55f48fc5\",\"schedule\":{\"time\":\"\",\"days\":[\"Friday\"]},\"rating\":{\"average\":8.7},\"weight\":99,\"network\":null,\"webChannel\":{\"id\":2,\"name\":\"Hulu\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"}},\"externals\":{\"tvrage\":6507,\"thetvdb\":73730,\"imdb\":\"tt0412253\"},\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/204/512460.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/204/512460.jpg\"},\"summary\":\"<p>In the wealthy, seaside community of Neptune, the rich and powerful make the rules. They own the town and are desperately trying to keep their dirty little secrets just that... secret. Unfortunately for them, there's Veronica Mars, a smart, fearless private investigator dedicated to solving the town's toughest mysteries. During the day, Veronica studies hard, but by night, she helps her father Keith with his private investigator business, sneaking through back alleys and scoping out no-tell motels with a telescopic-lens camera in an attempt to uncover the California beach town's darkest secrets.</p>\",\"updated\":1565438192,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/shows/473\"},\"previousepisode\":{\"href\":\"http://api.tvmaze.com/episodes/1638985\"}},\"_embedded\":{\"cast\":[{\"person\":{\"id\":22317,\"url\":\"http://www.tvmaze.com/people/22317/enrico-colantoni\",\"name\":\"Enrico Colantoni\",\"country\":{\"name\":\"Canada\",\"code\":\"CA\",\"timezone\":\"America/Halifax\"},\"birthday\":\"1963-02-14\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/37/93262.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/37/93262.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/22317\"}}},\"character\":{\"id\":80518,\"url\":\"http://www.tvmaze.com/characters/80518/veronica-mars-keith-mars\",\"name\":\"Keith Mars\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9299.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9299.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80518\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":27257,\"url\":\"http://www.tvmaze.com/people/27257/kristen-bell\",\"name\":\"Kristen Bell\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1980-07-18\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/159/398219.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/159/398219.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/27257\"}}},\"character\":{\"id\":80517,\"url\":\"http://www.tvmaze.com/characters/80517/veronica-mars-veronica-mars\",\"name\":\"Veronica Mars\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9297.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9297.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80517\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":4343,\"url\":\"http://www.tvmaze.com/people/4343/jason-dohring\",\"name\":\"Jason Dohring\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1982-03-30\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9643.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9643.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/4343\"}}},\"character\":{\"id\":80521,\"url\":\"http://www.tvmaze.com/characters/80521/veronica-mars-logan-echolls\",\"name\":\"Logan Echolls\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9298.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9298.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80521\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":46488,\"url\":\"http://www.tvmaze.com/people/46488/percy-daggs-iii\",\"name\":\"Percy Daggs III\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1982-07-20\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9641.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9641.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/46488\"}}},\"character\":{\"id\":80519,\"url\":\"http://www.tvmaze.com/characters/80519/veronica-mars-wallace-fennel\",\"name\":\"Wallace Fennel\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9303.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9303.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80519\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":4754,\"url\":\"http://www.tvmaze.com/people/4754/francis-capra\",\"name\":\"Francis Capra\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1983-04-27\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9642.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9642.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/4754\"}}},\"character\":{\"id\":80520,\"url\":\"http://www.tvmaze.com/characters/80520/veronica-mars-eli-weevil-navarro\",\"name\":\"Eli \\\"Weevil\\\" Navarro\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9301.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9301.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80520\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":16693,\"url\":\"http://www.tvmaze.com/people/16693/ryan-hansen\",\"name\":\"Ryan Hansen\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1981-07-05\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9648.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9648.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/16693\"}}},\"character\":{\"id\":80526,\"url\":\"http://www.tvmaze.com/characters/80526/veronica-mars-dick-casablancas\",\"name\":\"Dick Casablancas\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9305.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9305.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80526\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":46489,\"url\":\"http://www.tvmaze.com/people/46489/michael-muhney\",\"name\":\"Michael Muhney\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1975-06-12\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9647.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9647.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/46489\"}}},\"character\":{\"id\":80525,\"url\":\"http://www.tvmaze.com/characters/80525/veronica-mars-sheriff-don-lamb\",\"name\":\"Sheriff Don Lamb\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9309.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9309.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80525\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":35115,\"url\":\"http://www.tvmaze.com/people/35115/teddy-dunn\",\"name\":\"Teddy Dunn\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":null,\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/70/176246.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/70/176246.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/35115\"}}},\"character\":{\"id\":80529,\"url\":\"http://www.tvmaze.com/characters/80529/veronica-mars-duncan-kane\",\"name\":\"Duncan Kane\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9302.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9302.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80529\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":7437,\"url\":\"http://www.tvmaze.com/people/7437/tina-majorino\",\"name\":\"Tina Majorino\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1985-02-07\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9644.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9644.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/7437\"}}},\"character\":{\"id\":80522,\"url\":\"http://www.tvmaze.com/characters/80522/veronica-mars-cindy-mac-mackenzie\",\"name\":\"Cindy \\\"Mac\\\" Mackenzie\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9300.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9300.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80522\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":8298,\"url\":\"http://www.tvmaze.com/people/8298/julie-gonzalo\",\"name\":\"Julie Gonzalo\",\"country\":{\"name\":\"Argentina\",\"code\":\"AR\",\"timezone\":\"America/Argentina/Ushuaia\"},\"birthday\":\"1981-09-09\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/95/239604.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/95/239604.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/8298\"}}},\"character\":{\"id\":80524,\"url\":\"http://www.tvmaze.com/characters/80524/veronica-mars-parker-lee\",\"name\":\"Parker Lee\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9304.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9304.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80524\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":35143,\"url\":\"http://www.tvmaze.com/people/35143/chris-lowell\",\"name\":\"Chris Lowell\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1984-10-17\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9645.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9645.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/35143\"}}},\"character\":{\"id\":80523,\"url\":\"http://www.tvmaze.com/characters/80523/veronica-mars-stosh-piz-piznarski\",\"name\":\"Stosh \\\"Piz\\\" Piznarski\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9306.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9306.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80523\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":10409,\"url\":\"http://www.tvmaze.com/people/10409/kyle-gallner\",\"name\":\"Kyle Gallner\",\"country\":null,\"birthday\":null,\"deathday\":null,\"gender\":null,\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/2/5890.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/2/5890.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/10409\"}}},\"character\":{\"id\":80527,\"url\":\"http://www.tvmaze.com/characters/80527/veronica-mars-cassidy-beaver-casablancas\",\"name\":\"Cassidy \\\"Beaver\\\" Casablancas\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9308.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9308.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80527\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":54778,\"url\":\"http://www.tvmaze.com/people/54778/daran-norris\",\"name\":\"Daran Norris\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1964-11-01\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/10/25980.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/10/25980.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/54778\"}}},\"character\":{\"id\":644164,\"url\":\"http://www.tvmaze.com/characters/644164/veronica-mars-cliff-mccormack\",\"name\":\"Cliff McCormack\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644164\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":28102,\"url\":\"http://www.tvmaze.com/people/28102/tessa-thompson\",\"name\":\"Tessa Thompson\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1983-10-03\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/149/373576.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/149/373576.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/28102\"}}},\"character\":{\"id\":80530,\"url\":\"http://www.tvmaze.com/characters/80530/veronica-mars-jackie-cook\",\"name\":\"Jackie Cook\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9310.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9310.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80530\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":7709,\"url\":\"http://www.tvmaze.com/people/7709/max-greenfield\",\"name\":\"Max Greenfield\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1980-09-04\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/8060.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/8060.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/7709\"}}},\"character\":{\"id\":644587,\"url\":\"http://www.tvmaze.com/characters/644587/veronica-mars-leo-damato\",\"name\":\"Leo D'Amato\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644587\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":48783,\"url\":\"http://www.tvmaze.com/people/48783/dawnn-lewis\",\"name\":\"Dawnn Lewis\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1961-08-13\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/8/22413.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/8/22413.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/48783\"}}},\"character\":{\"id\":644802,\"url\":\"http://www.tvmaze.com/characters/644802/veronica-mars-marcia-langdon\",\"name\":\"Marcia Langdon\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644802\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":1507,\"url\":\"http://www.tvmaze.com/people/1507/david-starzyk\",\"name\":\"David Starzyk\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1961-07-14\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/190/475176.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/190/475176.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/1507\"}}},\"character\":{\"id\":644804,\"url\":\"http://www.tvmaze.com/characters/644804/veronica-mars-richard-casablancas\",\"name\":\"Richard Casablancas\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644804\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":20940,\"url\":\"http://www.tvmaze.com/people/20940/frank-gallegos\",\"name\":\"Frank Gallegos\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1970-08-03\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/86/215897.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/86/215897.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/20940\"}}},\"character\":{\"id\":644799,\"url\":\"http://www.tvmaze.com/characters/644799/veronica-mars-dodie-mendoza\",\"name\":\"Dodie Mendoza\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644799\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":6198,\"url\":\"http://www.tvmaze.com/people/6198/patton-oswalt\",\"name\":\"Patton Oswalt\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1969-01-27\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/0/1346.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/0/1346.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/6198\"}}},\"character\":{\"id\":644803,\"url\":\"http://www.tvmaze.com/characters/644803/veronica-mars-penn-epner\",\"name\":\"Penn Epner\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644803\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":34735,\"url\":\"http://www.tvmaze.com/people/34735/clifton-collins-jr\",\"name\":\"Clifton Collins, Jr.\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1970-06-16\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9963.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9963.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/34735\"}}},\"character\":{\"id\":644798,\"url\":\"http://www.tvmaze.com/characters/644798/veronica-mars-alonzo-lozano\",\"name\":\"Alonzo Lozano\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644798\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":820,\"url\":\"http://www.tvmaze.com/people/820/izabela-vidovic\",\"name\":\"Izabela Vidovic\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"2001-05-27\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/4/10651.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/4/10651.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/820\"}}},\"character\":{\"id\":644805,\"url\":\"http://www.tvmaze.com/characters/644805/veronica-mars-matty-ross\",\"name\":\"Matty Ross\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644805\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":18455,\"url\":\"http://www.tvmaze.com/people/18455/jk-simmons\",\"name\":\"J.K. Simmons\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1955-01-09\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/2/6103.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/2/6103.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/18455\"}}},\"character\":{\"id\":644843,\"url\":\"http://www.tvmaze.com/characters/644843/veronica-mars-clyde-prickett\",\"name\":\"Clyde Prickett\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644843\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":83289,\"url\":\"http://www.tvmaze.com/people/83289/kirby-howell-baptiste\",\"name\":\"Kirby Howell-Baptiste\",\"country\":{\"name\":\"United Kingdom\",\"code\":\"GB\",\"timezone\":\"Europe/London\"},\"birthday\":null,\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/178/447299.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/178/447299.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/83289\"}}},\"character\":{\"id\":644801,\"url\":\"http://www.tvmaze.com/characters/644801/veronica-mars-nicole-malloy\",\"name\":\"Nicole Malloy\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644801\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":21089,\"url\":\"http://www.tvmaze.com/people/21089/jacqueline-antaramian\",\"name\":\"Jacqueline Antaramian\",\"country\":null,\"birthday\":null,\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/8/20965.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/8/20965.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/21089\"}}},\"character\":{\"id\":644806,\"url\":\"http://www.tvmaze.com/characters/644806/veronica-mars-amalia-maloof\",\"name\":\"Amalia Maloof\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644806\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":159238,\"url\":\"http://www.tvmaze.com/people/159238/paul-karmiryan\",\"name\":\"Paul Karmiryan\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1991-08-13\",\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/126/317260.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/126/317260.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/159238\"}}},\"character\":{\"id\":644807,\"url\":\"http://www.tvmaze.com/characters/644807/veronica-mars-alex-maloof\",\"name\":\"Alex Maloof\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644807\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":1095,\"url\":\"http://www.tvmaze.com/people/1095/mido-hamada\",\"name\":\"Mido Hamada\",\"country\":{\"name\":\"Egypt\",\"code\":\"EG\",\"timezone\":\"Africa/Cairo\"},\"birthday\":null,\"deathday\":null,\"gender\":\"Male\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/120/300940.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/120/300940.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/1095\"}}},\"character\":{\"id\":644800,\"url\":\"http://www.tvmaze.com/characters/644800/veronica-mars-daniel-maloof\",\"name\":\"Daniel Maloof\",\"image\":null,\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/644800\"}}},\"self\":false,\"voice\":false},{\"person\":{\"id\":5578,\"url\":\"http://www.tvmaze.com/people/5578/sydney-tamiia-poitier\",\"name\":\"Sydney Tamiia Poitier\",\"country\":{\"name\":\"United States\",\"code\":\"US\",\"timezone\":\"America/New_York\"},\"birthday\":\"1973-11-15\",\"deathday\":null,\"gender\":\"Female\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9650.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9650.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/people/5578\"}}},\"character\":{\"id\":80528,\"url\":\"http://www.tvmaze.com/characters/80528/veronica-mars-mallory-dent\",\"name\":\"Mallory Dent\",\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_portrait/3/9307.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/3/9307.jpg\"},\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/characters/80528\"}}},\"self\":false,\"voice\":false}]}}";
	}
}