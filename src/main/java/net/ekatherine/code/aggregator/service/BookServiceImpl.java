package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.book.Book;
import net.ekatherine.code.aggregator.repository.interfaces.BookRepository;
import net.ekatherine.code.aggregator.service.interfaces.BookService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BookServiceImpl implements BookService
{
	private final BookRepository bookRepository;
	private final Util util;

	public BookServiceImpl(final BookRepository bookRepository, final Util util)
	{
		this.bookRepository = bookRepository;
		this.util = util;
	}

	@Override
	public Book getOne(final Long id)
	{
		return bookRepository.getOne(id);
	}

	@Override
	public Book save(final Book book)
	{
		return bookRepository.saveAndFlush(book);
	}

	@Override
	public Book update(final Book dest, final Book src)
	{
		util.consumeSuppliedIfTrue(dest::setDescription, src::getDescription, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setTitle, src::getTitle, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setPublishedAt, src::getPublishedAt, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setStatus, src::getStatus, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setAuthors, src::getAuthors, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setCategories, src::getCategories, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setIdentifiers, src::getIdentifiers, Objects::nonNull);
		util.consumeSuppliedIfTrue(dest::setPublishers, src::getPublishers, Objects::nonNull);

		return bookRepository.saveAndFlush(dest);
	}
}
