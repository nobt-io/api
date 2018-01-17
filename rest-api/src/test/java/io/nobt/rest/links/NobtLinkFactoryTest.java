package io.nobt.rest.links;

import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;

public class NobtLinkFactoryTest {

    @Test
    public void shouldCreateUri() {

        final NobtLinkFactory nobtLinkFactory = new NobtLinkFactory(new BasePath("http", "localhost:1234"));

        final Nobt nobt = aNobt().withId(new NobtId("foo")).build();
        final URI linkToNobt = nobtLinkFactory.createLinkToNobt(nobt);

        Assert.assertEquals("http://localhost:1234/nobts/foo", linkToNobt.toString());
    }
}