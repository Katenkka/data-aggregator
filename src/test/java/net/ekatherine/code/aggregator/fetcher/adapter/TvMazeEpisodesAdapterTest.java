package net.ekatherine.code.aggregator.fetcher.adapter;


import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.tv.Episode;
import net.ekatherine.code.aggregator.entity.tv.TvShow;
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
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TvMazeEpisodesAdapterTest
{
	@MockBean
	private FetcherUtil fetcherUtil;
	@SpyBean
	private Util util;

	private TvMazeEpisodesAdapter tvMazeEpisodesAdapter;

	@Before
	public void setUp()
	{
		tvMazeEpisodesAdapter = new TvMazeEpisodesAdapter(fetcherUtil, util);
	}

	@Test
	public void getExistingEntityReturnsTvShowEpisodesFilled() throws IOException
	{
		final String tvMazeId = "14667";
		final TvShow tvShow = new TvShow();
		tvShow.setTitle("MAIGRET");

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("http://api.tvmaze.com/shows/" + tvMazeId + "/episodes"), Mockito.anyInt())).thenReturn(getSuccessfulFullResponse());

		final List<Episode> expectedEpisodes = getExpectedEpisodes(tvShow);

		final List<Episode> episodes = tvMazeEpisodesAdapter.getEntities(tvMazeId, tvShow);

		Assert.assertEquals(expectedEpisodes, episodes);
	}

	private String getSuccessfulFullResponse()
	{
		return "[{\"id\":673998,\"url\":\"http://www.tvmaze.com/episodes/673998/maigret-2016-12-25-maigrets-dead-man\",\"name\":\"Maigret's Dead Man\",\"season\":2016,\"number\":2,\"airdate\":\"2016-12-25\",\"airtime\":\"21:00\",\"airstamp\":\"2016-12-25T21:00:00+00:00\",\"runtime\":120,\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_landscape/87/219824.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/87/219824.jpg\"},\"summary\":\"<p>In Maigret's Dead Man, a series of vicious, murderous attacks on three wealthy farms in Picardy hit the national headlines and the elite Brigade Criminelle at the Quay Des Orfevres is called upon to lend its expertise in tracking down the brutal gang responsible for the slaughter. However, Maigret is resolute in investigating the murder of an obscure anonymous Parisian. An investigation that ultimately solves both crimes.</p>\",\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/episodes/673998\"}}},{\"id\":668538,\"url\":\"http://www.tvmaze.com/episodes/668538/maigret-2016-03-28-maigret-sets-a-trap\",\"name\":\"Maigret Sets a Trap\",\"season\":2016,\"number\":1,\"airdate\":\"2016-03-28\",\"airtime\":\"21:00\",\"airstamp\":\"2016-03-28T20:00:00+00:00\",\"runtime\":120,\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_landscape/50/126921.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/50/126921.jpg\"},\"summary\":\"<p>The summer of 1955 finds Paris in the grip of both sweltering heat and terror on the streets. Four women have been murdered in Montmartre, with no apparent connection between the victims. Chief Inspector Maigret comes under huge pressure from the public and his superiors to apprehend the killer before he can strike again.</p>\",\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/episodes/668538\"}}},{\"id\":838588,\"url\":\"http://www.tvmaze.com/episodes/838588/maigret-2017-04-16-maigrets-night-at-the-crossroads\",\"name\":\"Maigret's Night at the Crossroads\",\"season\":2017,\"number\":1,\"airdate\":\"2017-04-16\",\"airtime\":\"20:00\",\"airstamp\":\"2017-04-16T19:00:00+00:00\",\"runtime\":120,\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_landscape/107/268841.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/107/268841.jpg\"},\"summary\":\"<p>Maigret faces a wall of silence from the inhabitants at a remote crossroads as he investigates the murder of a Jewish diamond dealer, who was killed in the dead of night.</p>\",\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/episodes/838588\"}}},{\"id\":838589,\"url\":\"http://www.tvmaze.com/episodes/838589/maigret-2017-12-24-maigret-in-montmartre\",\"name\":\"Maigret in Montmartre\",\"season\":2017,\"number\":2,\"airdate\":\"2017-12-24\",\"airtime\":\"20:30\",\"airstamp\":\"2017-12-24T20:30:00+00:00\",\"runtime\":115,\"image\":{\"medium\":\"http://static.tvmaze.com/uploads/images/medium_landscape/144/361240.jpg\",\"original\":\"http://static.tvmaze.com/uploads/images/original_untouched/144/361240.jpg\"},\"summary\":\"<p>Maigret investigates the random murders of a Countess and a Showgirl and discovers a dark secret that links their past lives at the Grand Hotel in Nice.</p>\",\"_links\":{\"self\":{\"href\":\"http://api.tvmaze.com/episodes/838589\"}}}]";
	}

	private List<Episode> getExpectedEpisodes(final TvShow tvShow) {
		final List<Episode> episodes = new ArrayList<>();

		{
			final Episode episode = new Episode(tvShow, "Maigret Sets a Trap", 1L, 2016L, LocalDateTime.of(2016, 3, 28, 20, 0).toInstant(ZoneOffset.UTC));
			episode.setDescription("The summer of 1955 finds Paris in the grip of both sweltering heat and terror on the streets. Four women have been murdered in Montmartre, with no apparent connection between the victims. Chief Inspector Maigret comes under huge pressure from the public and his superiors to apprehend the killer before he can strike again.");
			episode.setPosterUrl("http://static.tvmaze.com/uploads/images/original_untouched/50/126921.jpg");

			episodes.add(episode);
		}

		{
			final Episode episode = new Episode(tvShow, "Maigret's Dead Man", 2L, 2016L, LocalDateTime.of(2016, 12, 25, 21, 0).toInstant(ZoneOffset.UTC));
			episode.setDescription("In Maigret's Dead Man, a series of vicious, murderous attacks on three wealthy farms in Picardy hit the national headlines and the elite Brigade Criminelle at the Quay Des Orfevres is called upon to lend its expertise in tracking down the brutal gang responsible for the slaughter. However, Maigret is resolute in investigating the murder of an obscure anonymous Parisian. An investigation that ultimately solves both crimes.");
			episode.setPosterUrl("http://static.tvmaze.com/uploads/images/original_untouched/87/219824.jpg");

			episodes.add(episode);
		}

		{
			final Episode episode = new Episode(tvShow, "Maigret's Night at the Crossroads", 1L, 2017L, LocalDateTime.of(2017, 4, 16, 19, 0).toInstant(ZoneOffset.UTC));
			episode.setDescription("Maigret faces a wall of silence from the inhabitants at a remote crossroads as he investigates the murder of a Jewish diamond dealer, who was killed in the dead of night.");
			episode.setPosterUrl("http://static.tvmaze.com/uploads/images/original_untouched/107/268841.jpg");

			episodes.add(episode);
		}

		{
			final Episode episode = new Episode(tvShow, "Maigret in Montmartre", 2L, 2017L, LocalDateTime.of(2017, 12, 24, 20, 30).toInstant(ZoneOffset.UTC));
			episode.setDescription("Maigret investigates the random murders of a Countess and a Showgirl and discovers a dark secret that links their past lives at the Grand Hotel in Nice.");
			episode.setPosterUrl("http://static.tvmaze.com/uploads/images/original_untouched/144/361240.jpg");

			episodes.add(episode);
		}

		return episodes;
	}
}