package io.nobt.rest.constraints;

import io.nobt.core.domain.Share;
import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.nobt.core.domain.test.StaticPersonFactory.matthias;
import static io.nobt.core.domain.test.StaticPersonFactory.thomas;
import static io.nobt.core.domain.test.ShareFactory.randomShare;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckNoDuplicateDebtorsValidatorTest {

    private CheckNoDuplicateDebtorsValidator sut;

    @Before
    public void setUp() throws Exception {
        sut = new CheckNoDuplicateDebtorsValidator();
    }

    @Test
    public void shouldPassIfNoDuplicateNames() throws Exception {

        final List<Share> sharesWithoutDuplicateDebtors = Arrays.asList(randomShare(thomas), randomShare(matthias));

        final boolean result = sut.isValid(sharesWithoutDuplicateDebtors, null);

        assertTrue(result);
    }

    @Test
    public void shouldFailIfDuplicateNames() throws Exception {

        final List<Share> sharesWithoutDuplicateDebtors = Arrays.asList(randomShare(matthias), randomShare(matthias));

        final boolean result = sut.isValid(sharesWithoutDuplicateDebtors, null);

        assertFalse(result);
    }
}