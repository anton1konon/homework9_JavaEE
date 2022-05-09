package javaee.kononko.homework9.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ScriptAssert(lang = "javascript", script = "_this.password === _this.repeatPassword",
        message = "Password confirmation and password must be the same")
public class RegisterForm {
    @NotEmpty(message = "You must specify login")
    @Pattern(regexp = "([a-zA-Z]|\\d)+", message = "Login must contain only latin characters and numbers")
    private String login;
    @NotEmpty(message = "You must specify password")
    @Size(min = 8, max = 20, message = "Password must be at least 8 and at most 20 characters long.")
    private String password;
    private String repeatPassword;
}
