package net.ekatherine.code.aggregator.fetcher;

import net.ekatherine.code.aggregator.entity.book.Book;
import net.ekatherine.code.aggregator.fetcher.adapter.interfaces.ExternalSourceAdapter;
import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import net.ekatherine.code.aggregator.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
		return bookService.save(fetchedBook);
	}

	@Transactional
	@GetMapping(value = "/book/{id}/updateBy")
	public Book updateByIsbn(@PathVariable final Long id, @RequestParam final String isbn10) throws IOException, NoEntityFromExternalSourceFoundException
	{
		final Book book = bookService.getOne(id);
		final Book fetchedBook = externalSourceAdapter.getEntity(isbn10);
		return bookService.update(book, fetchedBook);
	}
}
