package net.ekatherine.code.aggregator.fetcher.adapter;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.game.Game;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.fetcher.util.FetcherUtil;
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
public class GiantBombGameAdapterTest
{
	private final String apiKey = "MYSECRETAPIKEY";
	@MockBean
	private FetcherUtil fetcherUtil;
	@SpyBean
	private Util util;

	private GiantBombGameAdapter giantBombGameAdapter;

	@Before
	public void setUp()
	{
		giantBombGameAdapter = new GiantBombGameAdapter(apiKey, fetcherUtil, util);
	}

	@Test(expected = NoEntityFromExternalSourceFoundException.class)
	public void getExceptionThrowsException() throws IOException, NoEntityFromExternalSourceFoundException
	{
		final String giantBombKey = "3030-3091300";

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("https://www.giantbomb.com/api/game/" + giantBombKey + "/?api_key=" + apiKey + "&format=json&field_list=themes,name,deck,aliases,concepts,developers,original_release_date,expected_release_day,expected_release_month,expected_release_year,genres,image"), Mockito.anyInt())).thenReturn("{ \"error\": \"Object Not Found\" }");

		final Game game = giantBombGameAdapter.getEntity(giantBombKey);
	}

	@Test
	public void getExistingEntityReturnsGameFilledWithAllFields() throws IOException, NoEntityFromExternalSourceFoundException
	{
		final String giantBombKey = "3030-30913";

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("https://www.giantbomb.com/api/game/" + giantBombKey + "/?api_key=" + apiKey + "&format=json&field_list=publishers,themes,name,deck,aliases,concepts,developers,original_release_date,expected_release_day,expected_release_month,expected_release_year,genres,image"), Mockito.anyInt())).thenReturn(getSuccessfulFullResponse());

		final Game game = giantBombGameAdapter.getEntity(giantBombKey);

		Assert.assertEquals("The Next Big Thing", game.getTitle());
		Assert.assertEquals("Hollywood Monsters 2", game.getAliases());
		Assert.assertEquals("An adventure game following the story of Dan Murray and Liz Allaire as they unravel a mystery filled with action and comedy. It's a spiritual sequel to previous P\u00E9ndulo Studios game, Hollywood Monsters. It's known in Spain as Hollywood Monsters 2.", game.getDescription());
		Assert.assertEquals("https://www.giantbomb.com/api/image/original/1738123-pack2d_tnbt.jpg", game.getPosterUrl());

		Assert.assertEquals(StatusEnum.UNKNOWN, game.getStatus());
		Assert.assertNull(game.getMetascore());

		Assert.assertEquals(LocalDateTime.of(2011, 4, 21, 0, 0).toInstant(ZoneOffset.UTC), game.getReleasedAt());

		Assert.assertEquals(Stream.of("P\u00E9ndulo Studios, S.L.").map(Party::new).collect(Collectors.toSet()), game.getDevelopers());
		Assert.assertEquals(Stream.of("Focus Home Interactive").map(Party::new).collect(Collectors.toSet()), game.getPublishers());

		Assert.assertEquals(Stream.of("Adventure").map(Subject::new).collect(Collectors.toSet()), game.getGenres());
		Assert.assertEquals(Stream.of("Comedy", "Crime").map(Subject::new).collect(Collectors.toSet()), game.getThemes());
		Assert.assertEquals(Stream.of("Mind Control", "Point and Click", "Multiple Protagonists", "Robots", "Steam", "Voice Acting", "Mummy", "Mad Scientist", "Late Title Card", "Crocodilians", "Steam Play", "Movie Reference").map(Subject::new).collect(Collectors.toSet()), game.getConcepts());

		Assert.assertEquals(Stream.of(new String[][]{
			{"giantBombGuid", giantBombKey},
		}).collect(Collectors.toMap(data -> data[0], data -> data[1])), game.getIdentifiers());
	}

	private String getSuccessfulFullResponse()
	{
		return "{\"error\":\"OK\",\"limit\":1,\"offset\":0,\"number_of_page_results\":1,\"number_of_total_results\":1,\"status_code\":1,\"results\":{\"aliases\":\"Hollywood Monsters 2\",\"deck\":\"An adventure game following the story of Dan Murray and Liz Allaire as they unravel a mystery filled with action and comedy. It's a spiritual sequel to previous P\\u00e9ndulo Studios game, Hollywood Monsters. It's known in Spain as Hollywood Monsters 2.\",\"expected_release_day\":21,\"expected_release_month\":4,\"expected_release_year\":2011,\"image\":{\"icon_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/square_avatar\\/1738123-pack2d_tnbt.jpg\",\"medium_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/scale_medium\\/1738123-pack2d_tnbt.jpg\",\"screen_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/screen_medium\\/1738123-pack2d_tnbt.jpg\",\"screen_large_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/screen_kubrick\\/1738123-pack2d_tnbt.jpg\",\"small_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/scale_small\\/1738123-pack2d_tnbt.jpg\",\"super_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/scale_large\\/1738123-pack2d_tnbt.jpg\",\"thumb_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/scale_avatar\\/1738123-pack2d_tnbt.jpg\",\"tiny_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/square_mini\\/1738123-pack2d_tnbt.jpg\",\"original_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/image\\/original\\/1738123-pack2d_tnbt.jpg\",\"image_tags\":\"All Images,Box Art\"},\"name\":\"The Next Big Thing\",\"original_release_date\":null,\"concepts\":[{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-62\\/\",\"id\":62,\"name\":\"Mind Control\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/mind-control\\/3015-62\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-75\\/\",\"id\":75,\"name\":\"Point and Click\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/point-and-click\\/3015-75\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-147\\/\",\"id\":147,\"name\":\"Multiple Protagonists\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/multiple-protagonists\\/3015-147\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-278\\/\",\"id\":278,\"name\":\"Robots\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/robots\\/3015-278\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-718\\/\",\"id\":718,\"name\":\"Steam\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/steam\\/3015-718\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-1413\\/\",\"id\":1413,\"name\":\"Voice Acting\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/voice-acting\\/3015-1413\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-1736\\/\",\"id\":1736,\"name\":\"Mummy\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/mummy\\/3015-1736\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-2438\\/\",\"id\":2438,\"name\":\"Mad Scientist\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/mad-scientist\\/3015-2438\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-3008\\/\",\"id\":3008,\"name\":\"Late Title Card\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/late-title-card\\/3015-3008\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-3901\\/\",\"id\":3901,\"name\":\"Crocodilians\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/crocodilians\\/3015-3901\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-4790\\/\",\"id\":4790,\"name\":\"Steam Play\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/steam-play\\/3015-4790\\/\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/concept\\/3015-5200\\/\",\"id\":5200,\"name\":\"Movie Reference\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/movie-reference\\/3015-5200\\/\"}],\"developers\":[{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/company\\/3010-1110\\/\",\"id\":1110,\"name\":\"P\\u00e9ndulo Studios, S.L.\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/pendulo-studios-sl\\/3010-1110\\/\"}],\"genres\":[{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/genre\\/3060-4\\/\",\"id\":4,\"name\":\"Adventure\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/games\\/?wikiSlug=adventure&wikiTypeId=3060&wikiId=4&genre=4\"}],\"publishers\":[{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/publisher\\/3010-720\\/\",\"id\":720,\"name\":\"Focus Home Interactive\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/focus-home-interactive\\/3010-720\\/\"}],\"themes\":[{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/theme\\/3032-6\\/\",\"id\":6,\"name\":\"Comedy\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/games\\/?wikiSlug=comedy&wikiTypeId=3032&wikiId=6&theme=6\"},{\"api_detail_url\":\"https:\\/\\/www.giantbomb.com\\/api\\/theme\\/3032-13\\/\",\"id\":13,\"name\":\"Crime\",\"site_detail_url\":\"https:\\/\\/www.giantbomb.com\\/games\\/?wikiSlug=crime&wikiTypeId=3032&wikiId=13&theme=13\"}]},\"version\":\"1.0\"}";
	}
}