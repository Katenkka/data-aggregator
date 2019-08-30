package net.ekatherine.code.aggregator.fetcher.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.book.Book;
import net.ekatherine.code.aggregator.fetcher.adapter.helper.ParsedEntity;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.fetcher.util.FetcherUtil;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class GoogleApiBookAdapter implements ExternalSourceAdapter<Book>
{
	private final FetcherUtil fetcherUtil;
	private final Util util;

	public GoogleApiBookAdapter(final FetcherUtil fetcherUtil, final Util util)
	{
		this.fetcherUtil = fetcherUtil;
		this.util = util;
	}

	@Override
	public Book getEntity(@NotBlank final String isbn10) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Entity parsedBook = (Entity) fetchExternalEntity("isbn:" + isbn10);

		return populateExternalContentToEntity(parsedBook);
	}

	private ParsedEntity fetchExternalEntity(final String key) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final String json = fetcherUtil.connectAndRetrieveJson("https://www.googleapis.com/books/v1/volumes?q=" + key, 10000);

		final Type type = new TypeToken<Volume>()
		{
		}.getType();

		final Volume container = new Gson().fromJson(json, type);

		if (Objects.isNull(container.items) || Array.getLength(container.items) == 0 || Objects.isNull(container.items[0].volumeInfo))
		{
			throw new NoEntityFromExternalSourceFoundException();
		}

		return container.items[0].volumeInfo;
	}

	private Book populateExternalContentToEntity(final ParsedEntity parsedEntity)
	{
		final Book book = new Book();
		final Entity parsedBook = (Entity) parsedEntity;

		book.setTitle(util.sanitize(parsedBook.title));
		book.setDescription(util.sanitize(parsedBook.description));

		Optional.ofNullable(parsedBook.authors)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.forEach(party -> book.addAuthor(new Party(util.sanitize(party))));

		Optional.ofNullable(parsedBook.publisher)
			.map(str -> Arrays.asList(str.split(",")))
			.orElseGet(Collections::emptyList)
			.forEach(party -> book.addPublisher(new Party(util.sanitize(party))));

		Optional.ofNullable(parsedBook.categories)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.forEach(subject -> book.addCategory(new Subject(util.sanitize(subject))));

		Optional.ofNullable(parsedBook.industryIdentifiers)
			.map(Arrays::stream)
			.orElseGet(Stream::empty)
			.forEach(identifier -> book.addIdentifier(identifier.type.toLowerCase(), util.sanitize(identifier.identifier)));

		if (Objects.nonNull(parsedBook.imageLinks) && Objects.nonNull(parsedBook.imageLinks.thumbnail))
		{
			book.setCoverUrl(util.sanitize(parsedBook.imageLinks.thumbnail));
		}

		if (Objects.nonNull(parsedBook.publishedDate))
		{
			book.setPublishedAt(Instant.parse(parsedBook.publishedDate + "T00:00:00.00Z"));
		}

		return book;
	}

	private static class Volume
	{
		Item[] items;
	}

	private static class Item
	{
		Entity volumeInfo;
	}

	private static class Entity implements ParsedEntity
	{
		Identifier[] industryIdentifiers;
		String title;
		String[] authors;
		String publisher;
		String publishedDate;
		String description;
		String[] categories;
		ImageLinks imageLinks;
	}

	private static class ImageLinks
	{
		String thumbnail;
	}

	private static class Identifier
	{
		String type;
		String identifier;
	}
}
