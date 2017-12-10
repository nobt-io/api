package db.migration.v12;

import io.nobt.util.Sets;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class V12_1_NobtEntityTest {

    @Test
    public void shouldConvertNames() throws Exception {

        final V12_1_NobtEntity entity = new V12_1_NobtEntity();
        entity.setExplicitParticipants_legacy("Thomas;David");


        entity.convert();


        assertThat(entity.getExplicitParticipants(), is(Sets.newHashSet(
                V12Person.forName("Thomas"),
                V12Person.forName("David")
        )));
    }

    @Test
    public void shouldSkipBlankNames() throws Exception {

        final V12_1_NobtEntity entity = new V12_1_NobtEntity();
        entity.setExplicitParticipants_legacy(";David;;");


        entity.convert();


        assertThat(entity.getExplicitParticipants(), is(Sets.newHashSet(
                V12Person.forName("David")
        )));
    }
}