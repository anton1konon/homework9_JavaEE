package javaee.kononko.homework9;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javaee.kononko.homework9.models.Book;
import javaee.kononko.homework9.models.BookForm;
import javaee.kononko.homework9.models.PageResponse;
import javaee.kononko.homework9.repositories.BookJPARepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookJPARepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBook() throws Exception {
        var form = new BookForm("Don Quixote", "Servantes", "85-359-0277-5");
        var json = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void addBook_onException_BadRequest() throws Exception {
        var form = new BookForm("Don Quixote", "Servantes", "ABCD");

        var json = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void allBooks() throws Exception {
        var books = List.of(
                Book.builder().Name("Don Quixote").Author("Servantes").Isbn("9780521485478").build(),
                Book.builder().Name("The Great Gatsby").Author("F. Scott Fitzgerald").Isbn("9780521485470").build()
        );

        Mockito.doReturn(new PageImpl<>(books, PageRequest.of(1, 10), books.size()))
                .when(bookRepository).findAll(Mockito.any(Pageable.class));

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=&page=1"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        var actual = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<PageResponse<Book>>() {
        });

        assertThat(actual.getList(), containsInAnyOrder(books.toArray()));
    }

    @Test
    void searchBooks() throws Exception {
        var books = List.of(
                Book.builder().Name("Don Quixote").Author("Servantes").Isbn("9780521485478").build(),
                Book.builder().Name("The Great Gatsby").Author("F. Scott Fitzgerald").Isbn("9780521485470").build()
        );

        var json = objectMapper.writeValueAsString(books);

        Mockito.doReturn(new PageImpl<>(books, PageRequest.of(1, 10), books.size()))
                .when(bookRepository).findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=9780&page=1"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        var actual = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<PageResponse<Book>>() {
        });

        assertThat(actual.getList(), containsInAnyOrder(books.toArray()));
    }

    @Test
    void searchBooksEmpty() throws Exception {
        Mockito.doReturn(new PageImpl<Book>(List.of(), PageRequest.of(1, 10), 0))
                .when(bookRepository).findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=9780&page=1"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        var actual = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<PageResponse<Book>>() {
        });

        assertThat(actual.getList(), emptyIterable());
    }
}
