package io.nobt.core.domain;

import io.nobt.core.UnknownNobtException;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NobtIdTest {

    @Test(expected = UnknownNobtException.class)
    public void givenIllegalIdentifier_onDecode_ShouldThrowUnknownNobtException() throws Exception {

        String illegalIdentifier = "fea3r3ffr";

        NobtId.fromExternalIdentifier(illegalIdentifier);
    }

    @Test
    public void shouldBeATwoWayFunction() throws Exception {

        final NobtId nobtId = new NobtId(1L);

        final String externalIdentifier = nobtId.toExternalIdentifier();
        final NobtId decodedNobtId = NobtId.fromExternalIdentifier(externalIdentifier);

        assertThat(decodedNobtId, is(nobtId));
    }
}