package core;

import models.Book;

import java.util.Collection;

public interface BookStore {
    void add(Book book);

    boolean contains(Book book);

    int size();

    Book getBook(String key);

    Book remove(String key);

    Collection<Book> removeSold();

    void sellBook(String key);

    void replace(Book oldBook, Book newBook);

    Collection<Book> getAllByAuthor(String author);

    Collection<Book> getAllByTitle(String title);

    Collection<Book> getByPriceRange(int lowerBoundCents, int upperBoundCents);

    Collection<Book> getAllOrderedByAuthorThenByTitle();
}
