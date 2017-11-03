package io.nobt.core.validation;

import io.nobt.core.domain.Amount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PositiveConstraintValidatorTest {

    private PositiveConstraintValidator sut;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext validatorContextMock;


    @Before
    public void setUp() throws Exception {
        sut = new PositiveConstraintValidator();
    }

    @Test
    public void givenNullValue_shouldReportFalse() throws Exception {

        final boolean result = validate(null);

        assertFalse(result);
    }

    @Test
    public void givenNegativeValue_shouldReportFalse() throws Exception {

        final boolean result = validate(amount(-1));

        assertFalse(result);
    }

    @Test
    public void givenPositiveValue_shouldReportTrue() throws Exception {

        final boolean result = validate(amount(1));

        assertTrue(result);
    }

    private boolean validate(Amount value) {
        return sut.isValid(value, validatorContextMock);
    }
}