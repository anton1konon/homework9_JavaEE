package javaee.kononko.homework9;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MyIsbnValidator implements ConstraintValidator<ValidISBN, String> {
    private final Pattern Isbn13 = Pattern.compile("^97(8|9)-\\d+-\\d+-\\d+-\\d$");
    private final Pattern Isbn10 = Pattern.compile("^\\d+-\\d+-\\d+-\\d$");
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Isbn13.matcher(s).matches()){
            return CheckNum(s.substring(4));
        } else if (Isbn10.matcher(s).matches()){
            return CheckNum(s);
        }
        return false;
    }

    private static boolean CheckNum(String s){
        var check = Character.getNumericValue(s.charAt(s.length()-1));
        var acc = 0;
        for (int c = 0, i = 1; c < s.length()-2; c++){
            var curr = s.charAt(c);
            if (Character.isDigit(curr)){
                var num = Character.getNumericValue(curr);
                acc += num * i;
                i++;
            }
        }
        return check == (acc - (acc/11)*11);
    }
}
