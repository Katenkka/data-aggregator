package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.book.Book;

import java.util.List;

public interface BookService extends GenericEntityService<Book>, UpdatableEntityService<Book>
{
	List<Book> findAll();
}