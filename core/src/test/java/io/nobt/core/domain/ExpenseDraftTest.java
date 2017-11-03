package io.nobt.core.domain;

import io.nobt.core.validation.CheckNoDuplicateDebtors;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ExpenseDraftTest {

    @Test
    public void typeOfFieldSharesShouldBeList() throws Exception {

        final Field sharesField = ExpenseDraft.class.getDeclaredField("shares");

        assertThat(sharesField.getType(), typeCompatibleWith(List.class));
    }

    @Test
    public void sharesFieldShouldValidateNoDuplicateDebtors() throws Exception {

        final CheckNoDuplicateDebtors annotation = ExpenseDraft.class.getDeclaredField("shares").getAnnotation(CheckNoDuplicateDebtors.class);

        assertThat(annotation, is(notNullValue()));
    }
}