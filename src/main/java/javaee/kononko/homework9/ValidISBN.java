package javaee.kononko.homework9;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MyIsbnValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidISBN {
    String message() default "Invalid ISBN format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
