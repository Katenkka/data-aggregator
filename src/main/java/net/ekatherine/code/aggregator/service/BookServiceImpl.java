package net.ekatherine.code.aggregator.service;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.component.Util;
import net.ekatherine.code.aggregator.entity.book.Book;
import net.ekatherine.code.aggregator.repository.interfaces.BookRepository;
import net.ekatherine.code.aggregator.service.interfaces.BookService;
import net.ekatherine.code.aggregator.service.interfaces.PartyService;
import net.ekatherine.code.aggregator.service.interfaces.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService
{
	private final BookRepository bookRepository;
	private final SubjectService subjectService;
	private final PartyService partyService;
	private final Util util;

	@Override
	public Book getOne(final Long id)
	{
		return bookRepository.getOne(id);
	}

	@Override
	public List<Book> findAll()
	{
		return bookRepository.findAll();
	}

	@Override
	public Page<Book> findAll(Pageable pageable)
	{
		return bookRepository.findAll(pageable);
	}

	@Override
	public Book save(final Book book)
	{
		book.setCategories(book.getCategories().stream().map(subjectService::replaceWithExisting).collect(Collectors.toSet()));

		book.setAuthors(book.getAuthors().stream().map(partyService::replaceWithExisting).collect(Collectors.toSet()));
		book.setPublishers(book.getPublishers().stream().map(partyService::replaceWithExisting).collect(Collectors.toSet()));

		return bookRepository.saveAndFlush(book);
	}

	@Override
	public Optional<Book> findByExtIdentifier(final String name, final String value) {
		return bookRepository.findOneByExternalIdentifier(name, value);
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

		dest.setUpdatedAt(Instant.now());
		return save(dest);
	}
}
