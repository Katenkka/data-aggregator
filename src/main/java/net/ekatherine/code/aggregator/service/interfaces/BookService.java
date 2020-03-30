package net.ekatherine.code.aggregator.service.interfaces;

import net.ekatherine.code.aggregator.entity.book.Book;

public interface BookService extends GenericEntityService<Book>, UpdatableEntityService<Book>, FindAllEntityService<Book>
{
}