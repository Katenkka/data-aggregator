package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.component.Constants;
import net.ekatherine.code.aggregator.entity.book.Book;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.BookService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController(value = "fetcherBookController")
public class BookController extends MainController
{
	private final ExternalSourceAdapter<Book> externalSourceAdapter;
	private final BookService bookService;

	public BookController(@Qualifier("googleApiBookAdapter") final ExternalSourceAdapter<Book> externalSourceAdapter, final BookService bookService)
	{
		this.externalSourceAdapter = externalSourceAdapter;
		this.bookService = bookService;
	}

	@Transactional
	@GetMapping(value = "/book/addBy")
	public Book addByIsbn(@RequestParam final String isbn10) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Book fetchedBook = externalSourceAdapter.getEntity(isbn10);
		final Optional<Book> existing = bookService.findByExtIdentifier(isbn10);
		if (existing.isPresent()) {
			return bookService.update(existing.get(), fetchedBook);
		}
		return bookService.save(fetchedBook);
	}

	@Transactional
	@GetMapping(value = "/book/{id}/updateBy")
	public Book updateByIsbn(@PathVariable final Long id, @RequestParam final String isbn10) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Book book = bookService.getOne(id);
		return updateBookByExternalId(book, isbn10);
	}

	@GetMapping(value = "/book/updateAllByIsbn")
	@ResponseStatus(HttpStatus.OK)
	public void updateAllByIsbn(@PageableDefault(size = 200)
								@SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.ASC)})
								Pageable pageable)
	{
		Page<Book> bookPage = bookService.findAll(pageable);
		if(bookPage.isEmpty()) {
			return;
		}

		bookPage.get().forEach(book -> {
			try {
				final String isbnKey = book.getIdentifiers().get(Constants.ISBN_10_ID);
				updateBookByExternalId(book, isbnKey);
			} catch (final Throwable e) {
				LoggerFactory.getLogger(getClass()).debug("Something went wrong while updating Book with id = {}", book.getId());
				LoggerFactory.getLogger(getClass()).error("Message: ", e);
			}
		});
	}

	private Book updateBookByExternalId(final Book book, final String isbnKey) throws IOException, NoEntityFromExternalSourceFoundException {
		final Book fetchedBook = externalSourceAdapter.getEntity(isbnKey);
		return bookService.update(book, fetchedBook);
	}
}
