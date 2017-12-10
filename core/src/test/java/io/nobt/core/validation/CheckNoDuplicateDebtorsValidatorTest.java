package io.nobt.core.validation;

import io.nobt.core.domain.Share;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

import static io.nobt.test.domain.factories.ShareFactory.randomShare;
import static io.nobt.test.domain.factories.StaticPersonFactory.matthias;
import static io.nobt.test.domain.factories.StaticPersonFactory.thomas;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CheckNoDuplicateDebtorsValidatorTest {

    private CheckNoDuplicateDebtorsValidator sut;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext validatorContextMock;

    @Before
    public void setUp() throws Exception {
        sut = new CheckNoDuplicateDebtorsValidator();
    }

    @Test
    public void shouldPassIfNoDuplicateNames() throws Exception {

        final List<Share> sharesWithoutDuplicateDebtors = Arrays.asList(randomShare(thomas), randomShare(matthias));

        final boolean result = sut.isValid(sharesWithoutDuplicateDebtors, validatorContextMock);

        assertTrue(result);
    }

    @Test
    public void shouldFailIfDuplicateNames() throws Exception {

        final List<Share> sharesWithoutDuplicateDebtors = Arrays.asList(randomShare(matthias), randomShare(matthias));

        final boolean result = sut.isValid(sharesWithoutDuplicateDebtors, validatorContextMock);

        assertFalse(result);
    }
}