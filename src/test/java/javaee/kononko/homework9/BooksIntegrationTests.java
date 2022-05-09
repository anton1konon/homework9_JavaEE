package javaee.kononko.homework9;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import javaee.kononko.homework9.models.Book;
import javaee.kononko.homework9.models.BookForm;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksIntegrationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }

    @Test
    void addBookAndFetchBook_emptyQueryReturnsAll() throws Exception {
        var form = new BookForm("Ulysses", "James Joyce", "93-86954-21-4");
        var json = objectMapper.writeValueAsString(form);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());


        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=uly&page=1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("list", Book.class);

        var expected =  Book.builder().Name(form.getName()).Author(form.getAuthor()).Isbn(form.getIsbn()).build();
        assertThat(actual, hasItem(expected));
    }


    @Test
    void addBookAndSearchBook_success() throws Exception {
        var form = new BookForm("In Search of Lost Time", "Marcel Proust", "960-425-059-0");
        var json = objectMapper.writeValueAsString(form);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        var query = "Search";

        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=" + query + "&page=1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .extract()
                .jsonPath()
                .getList("list", Book.class);

        assertThat(actual, hasItem(Book.builder().Name(form.getName()).Author(form.getAuthor()).Isbn(form.getIsbn()).build()));
    }

    @Test
    void addBookAndSearchWrongQuery_notContains() throws Exception {
        var form = new BookForm("One Hundred Years of Solitude", "Gabriel Garcia Marquez", "9971-5-0210-0");
        var json = objectMapper.writeValueAsString(form);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());


        var query = "SomeBook";

        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=" + query + "&page=1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .extract()
                .jsonPath()
                .getList("list", Book.class);
        assertThat(actual, not(hasItem(Book.builder().Name(form.getName()).Author(form.getAuthor()).Isbn(form.getIsbn()).build())));
    }

    @Test
    void addMultipleAndSearch_containsOne() throws Exception {
        var form1 = new BookForm("The Great Gatsby", "F. Scott Fitzgerald", "85-359-0277-5");
        var json = objectMapper.writeValueAsString(form1);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        var form2 = new BookForm("Don Quixote", "Servantes", "1-84356-028-3");
        var json2 = objectMapper.writeValueAsString(form2);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json2)
                .when()
                .post("/addBook")
                .then();

        var query = "359";

        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=" + query + "&page=1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("list", Matchers.hasSize(1))
                .extract()
                .jsonPath()
                .getList("list", Book.class);

        assertThat(actual, hasItem(Book.builder().Name(form1.getName()).Author(form1.getAuthor()).Isbn(form1.getIsbn()).build()));
    }
}
