package org.restaurant.voting.util.validation;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SafeHtmlValidator implements ConstraintValidator<SafeHtml, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        return value == null || Jsoup.clean(value, Safelist.none()).equals(value);
    }
}
