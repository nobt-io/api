package io.nobt.core.domain;

import io.nobt.core.domain.debt.combination.CombinationResult;
import io.nobt.core.domain.debt.combination.CompositeResult;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.nobt.test.domain.matchers.CombinationResultMatchers.hasChanges;
import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class CompositeResultTest {

    @Test
    @Parameters
    public void shouldAccumulateHasChangesFlag(CombinationResult composite, boolean expected) throws Exception {
        assertThat(composite, hasChanges(equalTo(expected)));
    }

    private static Object[] parametersForShouldAccumulateHasChangesFlag() {
        return $(
                $(
                        new CompositeResult(
                                new HasChangesResult(),
                                new HasNoChangesResult()
                        ),
                        true
                ),
                $(
                        new CompositeResult(
                                new HasChangesResult()
                        ),
                        true
                ),
                $(
                        new CompositeResult(
                                new HasNoChangesResult()
                        ),
                        false
                ),
                $(
                        new CompositeResult(
                                new HasNoChangesResult(),
                                new HasChangesResult(),
                                new HasNoChangesResult()
                        ),
                        true
                ),
                $(
                        new CompositeResult(),
                        false
                )
        );
    }
}