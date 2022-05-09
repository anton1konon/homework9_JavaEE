package javaee.kononko.homework9;

import javaee.kononko.homework9.models.BookForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;


public class BookValidationTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCorrectBookSuccess() {
        var book = new BookForm("Ulysses", "James Joyce", "93-86954-21-4");

        var violations = validator.validate(book);

        assertThat(violations, is(empty()));
    }

    @Test
    void nullName_error() {
        var book = new BookForm(null, "James Joyce", "93-86954-21-4");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }

    @Test
    void nullAuthor_error() {
        var book = new BookForm("Ulysses", null, "93-86954-21-4");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }

    @Test
    void emptyName_error() {
        var book = new BookForm("", "James Joyce", "93-86954-21-4");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }

    @Test
    void emptyAuthor_error() {
        var book = new BookForm("Ulysses", "", "93-86954-21-4");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }

    @Test
    void incorrectIsbn_error() {
        var book = new BookForm("Ulysses", "James Joyce", "aaa-vdd");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }

    @Test
    void incorrectIsbnCheckNum_error() {
        var book = new BookForm("Ulysses", "James Joyce", "93-86954-21-6");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }

    @Test
    void incorrectIsbn13_error() {
        var book = new BookForm("Ulysses", "James Joyce", "925-93-86954-21-6");

        var violations = validator.validate(book);

        assertThat(violations, is(not(empty())));
    }
}
