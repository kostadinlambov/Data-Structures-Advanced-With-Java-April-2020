package core;

import models.Book;

import java.util.*;
import java.util.stream.Collectors;

public class BookLand implements BookStore {
    private final Map<String, Book> bookMapByKey;
    private final Set<Book> booksSetOrdered;

    private final Map<String, Set<Book>> booksByAuthor;
    private final Map<String, Set<Book>> booksByTitle;

    public BookLand() {
        this.bookMapByKey = new HashMap<>();
        this.booksSetOrdered = new TreeSet<>(Comparator.comparing(Book::getAuthor).thenComparing(Book::getTitle));

        this.booksByAuthor = new HashMap<>();
        this.booksByTitle = new HashMap<>();
    }

    @Override
    public void add(Book book) {
        boolean contains = this.contains(book);

        if (contains) {
            throw new IllegalArgumentException();
        }

        this.bookMapByKey.put(book.getKey(), book);
        this.booksSetOrdered.add(book);

        this.booksByAuthor.putIfAbsent(book.getAuthor(), new TreeSet<>(Comparator.comparingInt(Book::getPriceCents).thenComparing(Book::getTitle)));
        this.booksByAuthor.get(book.getAuthor()).add(book);

        this.booksByTitle.putIfAbsent(book.getTitle(), new TreeSet<>(Comparator.comparingInt(Book::getPriceCents).thenComparing(Book::getTitle)));
        this.booksByTitle.get(book.getTitle()).add(book);
    }

    @Override
    public boolean contains(Book book) {
        return this.booksSetOrdered.contains(book);
    }

    @Override
    public int size() {
        return this.bookMapByKey.size();
    }

    @Override
    public Book getBook(String key) {

        Book book = this.bookMapByKey.get(key);

        if (book == null) {
            throw new IllegalArgumentException();
        }

        return book;
    }

    @Override
    public Book remove(String key) {
        Book removedBook = this.bookMapByKey.remove(key);

        if (removedBook == null) {
            throw new IllegalArgumentException();
        }

        this.booksSetOrdered.remove(removedBook);

        this.booksByAuthor.get(removedBook.getAuthor()).removeIf(book -> book.getKey().equals(key));

        return removedBook;
    }

    @Override
    public Collection<Book> removeSold() {
        List<Book> soldBooks = this.bookMapByKey.values().stream()
                .filter(Book::isSold).collect(Collectors.toList());

        for (Book soldBook : soldBooks) {
            this.bookMapByKey.remove(soldBook.getKey());
            this.booksSetOrdered.remove(soldBook);
            this.booksByAuthor.get(soldBook.getAuthor()).removeIf(book -> book.getKey().equals(soldBook.getKey()));
            this.booksByTitle.get(soldBook.getTitle()).removeIf(book -> book.getKey().equals(soldBook.getKey()));
        }

        return soldBooks;
    }

    @Override
    public void sellBook(String key) {
        Book book = this.bookMapByKey.get(key);

        if (book == null) {
            throw new IllegalArgumentException();
        }

        book.setSold(true);
    }

    @Override
    public void replace(Book oldBook, Book newBook) {
        boolean containsOld = this.contains(oldBook);

        if (!containsOld) {
            throw new UnsupportedOperationException();
        }

        boolean containsNew = this.contains(newBook);

        if (containsNew) {
            throw new UnsupportedOperationException();
        }

        this.remove(oldBook.getKey());
        this.add(newBook);
    }

    @Override
    public Collection<Book> getAllByAuthor(String author) {
        return this.booksByAuthor.get(author);
    }

    @Override
    public Collection<Book> getAllByTitle(String title) {
        return this.booksByTitle.get(title);
    }

    @Override
    public Collection<Book> getByPriceRange(int lowerBoundCents, int upperBoundCents) {
        return this.bookMapByKey.values().stream()
                .filter(book -> book.getPriceCents() >= lowerBoundCents && book.getPriceCents() < upperBoundCents)
                .sorted(Comparator.comparingInt(Book::getPriceCents).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> getAllOrderedByAuthorThenByTitle() {
        return this.booksSetOrdered;
    }
}
