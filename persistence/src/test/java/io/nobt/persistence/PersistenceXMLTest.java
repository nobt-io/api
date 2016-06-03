package io.nobt.persistence;

import org.hamcrest.CoreMatchers;
import org.hibernate.jpa.boot.internal.ParsedPersistenceXmlDescriptor;
import org.hibernate.jpa.boot.internal.PersistenceXmlParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PersistenceXMLTest {

    @Test
    public void persistenceXmlMustSetHbm2DDLToValidate() throws Exception {

        final List<ParsedPersistenceXmlDescriptor> descriptors = PersistenceXmlParser.locatePersistenceUnits(Collections.emptyMap());

        final ParsedPersistenceXmlDescriptor productionPersistenceXml = descriptors.stream().filter(d -> d.getName().equals("persistence")).findFirst().orElseThrow(IllegalStateException::new);

        final Properties properties = productionPersistenceXml.getProperties();

        final String hbm2ddlValue = properties.getProperty("hibernate.hbm2ddl.auto");

        Assert.assertThat(hbm2ddlValue, CoreMatchers.is("validate"));
    }
}
