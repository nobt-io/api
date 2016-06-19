package io.nobt.core.domain;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;

public class PersonTest {

    @Test
    public void shouldValidateNameToForbidSemicolons() throws Exception {

        final Field name = Person.class.getDeclaredField("name");
        final Pattern annotation = name.getAnnotation(Pattern.class);
        final String regexp = annotation.regexp();

        Assert.assertThat(regexp, Matchers.is("^;"));
    }
}