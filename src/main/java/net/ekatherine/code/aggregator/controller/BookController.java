package net.ekatherine.code.aggregator.controller;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.entity.book.Book;
import net.ekatherine.code.aggregator.service.interfaces.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController extends MainController
{
	private final BookService bookService;

	@GetMapping(value = "/book/{id}")
	public Book getById(@PathVariable final Long id)
	{
		return bookService.getOne(id);
	}
}
