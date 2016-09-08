package io.nobt.persistence;

import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PersistenceXMLTest {

    @Test
    public void persistenceXmlMustSetHbm2DDLToValidate() throws Exception {

        final List<ParsedPersistenceXmlDescriptor> descriptors = PersistenceXmlParser.locatePersistenceUnits(Collections.emptyMap());

        final ParsedPersistenceXmlDescriptor productionPersistenceXml = descriptors.stream().filter(d -> d.getName().equals("persistence")).findFirst().orElseThrow(IllegalStateException::new);

        final Properties properties = productionPersistenceXml.getProperties();

        final String hbm2ddlValue = properties.getProperty("hibernate.hbm2ddl.auto");

        assertThat(hbm2ddlValue, is("validate"));
    }
}
