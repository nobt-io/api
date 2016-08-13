package io.nobt.persistence.mapping;

import io.nobt.core.domain.Share;
import io.nobt.util.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static io.nobt.core.domain.test.StaticPersonFactory.david;
import static io.nobt.core.domain.test.StaticPersonFactory.harald;
import static io.nobt.core.domain.test.ShareFactory.randomShare;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class ShareMapperTest {

    private ShareMapper sut;

    @Before
    public void setUp() throws Exception {
        sut = new ShareMapper();
    }

    @Test
    public void shouldMapSetOfSharesToBinaryAndBack() throws Exception {

        final Share firstShare = randomShare(david);
        final Share secondShare = randomShare(harald);

        final byte[] sharesAsBytes = sut.mapToByteArray(Arrays.asList(firstShare, secondShare));

        final Set<Share> shares = sut.mapToShareSet(sharesAsBytes);

        assertThat(shares, containsInAnyOrder(firstShare, secondShare));
    }
}