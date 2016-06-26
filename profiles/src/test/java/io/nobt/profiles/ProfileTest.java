package io.nobt.profiles;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProfileTest {

    @Test
    public void shouldDefaultToStandaloneProfile() throws Exception {
        final Profile actualProfile = Profile.getCurrentProfile();

        assertThat(actualProfile, is(Profile.STANDALONE));
    }
}