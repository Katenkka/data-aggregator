package net.ekatherine.code.aggregator.fetcher.adapter;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.book.Book;
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
public class GoogleApiBookAdapterTest
{
	@MockBean
	private FetcherUtil fetcherUtil;
	@SpyBean
	private Util util;

	private GoogleApiBookAdapter googleApiBookAdapter;

	@Before
	public void setUp()
	{
		googleApiBookAdapter = new GoogleApiBookAdapter(fetcherUtil, util);
	}

	@Test(expected = NoEntityFromExternalSourceFoundException.class)
	public void getExceptionThrowsException() throws IOException, NoEntityFromExternalSourceFoundException
	{
		final String isbn10 = "080417072X";

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn10), Mockito.anyInt())).thenReturn("{ \"json\": \"error\" }");

		final Book book = googleApiBookAdapter.getEntity(isbn10);
	}

	@Test
	public void getExistingEntityReturnsBookFilledWithAllFields() throws IOException, NoEntityFromExternalSourceFoundException
	{
		final String isbn10 = "080417072X";

		Mockito.when(fetcherUtil.connectAndRetrieveJson(Mockito.eq("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn10), Mockito.anyInt())).thenReturn(getSuccessfulFullResponse());

		final Book book = googleApiBookAdapter.getEntity(isbn10);

		Assert.assertEquals("Mr. Kiss and Tell", book.getTitle());
		Assert.assertEquals("http://books.google.com/books/content?id=N_SLDQAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api", book.getCoverUrl());
		Assert.assertEquals("\"The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel\" --", book.getDescription());

		Assert.assertEquals(StatusEnum.UNKNOWN, book.getStatus());

		Assert.assertEquals(LocalDateTime.of(2015, 1, 20, 0, 0).toInstant(ZoneOffset.UTC), book.getPublishedAt());

		Assert.assertEquals(Stream.of("Rob Thomas", "Jennifer Graham").map(Party::new).collect(Collectors.toSet()), book.getAuthors());

		Assert.assertEquals(Stream.of("Vintage").map(Party::new).collect(Collectors.toSet()), book.getPublishers());

		Assert.assertEquals(Stream.of(new String[][]{
			{"isbn_13", "9780804170727"},
			{"isbn_10", "080417072X"},
		}).collect(Collectors.toMap(data -> data[0], data -> data[1])), book.getIdentifiers());
	}

	private String getSuccessfulFullResponse()
	{
		return "{\n" +
			" \"kind\": \"books#volumes\",\n" +
			" \"totalItems\": 1,\n" +
			" \"items\": [\n" +
			"  {\n" +
			"   \"kind\": \"books#volume\",\n" +
			"   \"id\": \"N_SLDQAAQBAJ\",\n" +
			"   \"etag\": \"Gw3N9egzZ9s\",\n" +
			"   \"selfLink\": \"https://www.googleapis.com/books/v1/volumes/N_SLDQAAQBAJ\",\n" +
			"   \"volumeInfo\": {\n" +
			"    \"title\": \"Mr. Kiss and Tell\",\n" +
			"    \"authors\": [\n" +
			"     \"Rob Thomas\",\n" +
			"     \"Jennifer Graham\"\n" +
			"    ],\n" +
			"    \"publisher\": \"Vintage\",\n" +
			"    \"publishedDate\": \"2015-01-20\",\n" +
			"    \"description\": \"\\\"The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel\\\" --\",\n" +
			"    \"industryIdentifiers\": [\n" +
			"     {\n" +
			"      \"type\": \"ISBN_13\",\n" +
			"      \"identifier\": \"9780804170727\"\n" +
			"     },\n" +
			"     {\n" +
			"      \"type\": \"ISBN_10\",\n" +
			"      \"identifier\": \"080417072X\"\n" +
			"     }\n" +
			"    ],\n" +
			"    \"readingModes\": {\n" +
			"     \"text\": false,\n" +
			"     \"image\": false\n" +
			"    },\n" +
			"    \"pageCount\": 336,\n" +
			"    \"printType\": \"BOOK\",\n" +
			"    \"categories\": [\n" +
			"     \"Fiction\"\n" +
			"    ],\n" +
			"    \"averageRating\": 4.0,\n" +
			"    \"ratingsCount\": 19,\n" +
			"    \"maturityRating\": \"NOT_MATURE\",\n" +
			"    \"allowAnonLogging\": false,\n" +
			"    \"contentVersion\": \"0.1.0.0.preview.0\",\n" +
			"    \"panelizationSummary\": {\n" +
			"     \"containsEpubBubbles\": false,\n" +
			"     \"containsImageBubbles\": false\n" +
			"    },\n" +
			"    \"imageLinks\": {\n" +
			"     \"smallThumbnail\": \"http://books.google.com/books/content?id=N_SLDQAAQBAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api\",\n" +
			"     \"thumbnail\": \"http://books.google.com/books/content?id=N_SLDQAAQBAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api\"\n" +
			"    },\n" +
			"    \"language\": \"en\",\n" +
			"    \"previewLink\": \"http://books.google.pl/books?id=N_SLDQAAQBAJ&dq=isbn:080417072X&hl=&cd=1&source=gbs_api\",\n" +
			"    \"infoLink\": \"http://books.google.pl/books?id=N_SLDQAAQBAJ&dq=isbn:080417072X&hl=&source=gbs_api\",\n" +
			"    \"canonicalVolumeLink\": \"https://books.google.com/books/about/Mr_Kiss_and_Tell.html?hl=&id=N_SLDQAAQBAJ\"\n" +
			"   },\n" +
			"   \"saleInfo\": {\n" +
			"    \"country\": \"PL\",\n" +
			"    \"saleability\": \"NOT_FOR_SALE\",\n" +
			"    \"isEbook\": false\n" +
			"   },\n" +
			"   \"accessInfo\": {\n" +
			"    \"country\": \"PL\",\n" +
			"    \"viewability\": \"NO_PAGES\",\n" +
			"    \"embeddable\": false,\n" +
			"    \"publicDomain\": false,\n" +
			"    \"textToSpeechPermission\": \"ALLOWED\",\n" +
			"    \"epub\": {\n" +
			"     \"isAvailable\": false\n" +
			"    },\n" +
			"    \"pdf\": {\n" +
			"     \"isAvailable\": false\n" +
			"    },\n" +
			"    \"webReaderLink\": \"http://play.google.com/books/reader?id=N_SLDQAAQBAJ&hl=&printsec=frontcover&source=gbs_api\",\n" +
			"    \"accessViewStatus\": \"NONE\",\n" +
			"    \"quoteSharingAllowed\": false\n" +
			"   },\n" +
			"   \"searchInfo\": {\n" +
			"    \"textSnippet\": \"&quot;The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel&quot; --\"\n" +
			"   }\n" +
			"  }\n" +
			" ]\n" +
			"}\n";
	}
}