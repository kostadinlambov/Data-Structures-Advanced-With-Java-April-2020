package core;

import models.Book;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.List;

import static org.junit.Assert.*;

public class BookStoreTest {

    private BookStore bookStore;
    private List<Book> books;

    @Before
    public void setUp() {
        this.bookStore = new BookLand();
        this.books = new ArrayList<>();

        Map<String, String[]> table = Map.of(
                "Richard Dawkins", new String[]{
                        "The Selfish Gene",
                        "Science in the Soul"
                },
                "Terry Pratchett", new String[]{
                        "Mort",
                        "Reaper Man",
                        "Small Gods",
                        "Hogfather"
                },
                "Sam Harris", new String[]{
                        "The End of Faith",
                        "The Moral Landscape"
                },
                "Stephen King", new String[]{
                        "Salem's Lot",
                        "The Dark Tower: The Gunslinger"
                },
                "J. R. R. Tolkien", new String[]{
                        "The Hobbit",
                        "The Lord of the Rings"
                }
        );

        int priceCentsBegin = 1000;

        for (Map.Entry<String, String[]> entry : table.entrySet()) {
            String author = entry.getKey();
            for (String title : entry.getValue()) {
                Book book = new Book(author, title, priceCentsBegin += 200);
                this.bookStore.add(book);
                this.books.add(book);
            }
        }
    }

    @Test
    public void add() {
        BookStore newBookStore = new BookLand();
        Book book = new Book("A", "B", 100);
        assertFalse(newBookStore.contains(book));
        newBookStore.add(book);
        assertTrue(newBookStore.contains(book));
        for (Book b : books) {
            assertTrue(this.bookStore.contains(b));
        }
    }

    @Test
    public void contains() {
        assertTrue(this.bookStore.contains(this.books.get(0)));
        assertFalse(this.bookStore.contains(new Book("Richard Dawkins", "The God Delusion", 9999)));
    }

    @Test
    public void size() {
        assertEquals(0, new BookLand().size());
        assertEquals(this.books.size(), this.bookStore.size());
    }

    @Test
    public void get() {
        String key = this.books.get(this.books.size() - 1).getKey();
        Book book = this.bookStore.getBook(key);
        assertNotNull(book);
        assertEquals(key, book.getKey());
    }

    @Test
    public void remove() {
        String key = this.books.get(this.books.size() - 1).getKey();
        Book book = this.bookStore.remove(key);
        assertNotNull(book);
        assertEquals(key, book.getKey());
    }

    @Test
    public void removeSold() {
        for (Book book : books) {
            book.setSold(true);
        }

        Iterable<Book> books = this.bookStore.removeSold();

        assertNotNull(books);

        List<Book> actual = new ArrayList<>();

        books.forEach(actual::add);

        List<Book> expected = new ArrayList<>(this.books);

        expected.sort(Comparator.comparing(Book::getKey));
        actual.sort(Comparator.comparing(Book::getKey));

        assertEquals(expected.size(), actual.size());

        assertEquals(expected, actual);
    }

    @Test
    public void sellBook() {
        Book book = this.books.get(0);
        String key = book.getKey();

        this.bookStore.sellBook(key);

        assertTrue(book.isSold());
    }
}