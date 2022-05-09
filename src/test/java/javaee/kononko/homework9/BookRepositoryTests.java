package javaee.kononko.homework9;

import javaee.kononko.homework9.models.Book;
import javaee.kononko.homework9.repositories.BookJPARepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class BookRepositoryTests {
    @Autowired
    private BookJPARepository repository;

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void allBooks_does_not_throw(){
        repository.findAll();
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void allBooks_contains_two(){
        var books = repository.findAll(Pageable.unpaged());
        assertThat(books.getContent(), hasSize(2));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_books_contains_donQuixote(){
        var list = repository.findAll(BookSpecs.searchBooksSpec("Don Quixote"));
        assertThat(list, hasItem(hasProperty("name", equalTo("Don Quixote"))));
        assertThat(list, not(hasItem(hasProperty("name", equalTo("The Great Gatsby")))));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_books_contains_nothing(){
        var list = repository.findAll(BookSpecs.searchBooksSpec("@#$%"));
        assertThat(list, hasSize(0));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_success(){
        var book = Book.builder().Name("Don Quixote").Author("Servantes").Isbn("123456").build();
        var added = repository.save(new Book(null, book.getName(), book.getAuthor(), book.getIsbn()));
        assertThat(added, equalTo(book));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_and_get_book_by_id(){
        var book = Book.builder().Name("Don Quixote").Author("Servantes").Isbn("123456").build();
        var id = repository.save(new Book(null, book.getName(), book.getAuthor(), book.getIsbn())).getId();
        var loaded = repository.findById(id);
        assertThat(loaded, equalTo(Optional.of(book)));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_and_get_books_contains_added(){
        var book = Book.builder().Name("War and Peace").Author("Leo Tolstoy").Isbn("9780786112517").build();
        repository.save(new Book(null, book.getName(), book.getAuthor(), book.getIsbn()));
        var books = repository.findAll();
        assertThat(books, hasItem(book));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_and_search_books_contains_added(){
        var book = Book.builder().Name("War and Peace").Author("Leo Tolstoy").Isbn("9780786112517").build();
        repository.save(new Book(null, book.getName(), book.getAuthor(), book.getIsbn()));
        var books = repository.findAll(BookSpecs.searchBooksSpec(book.getIsbn()));
        assertThat(books, hasItem(book));
    }
}
