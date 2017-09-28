package io.nobt.application.env;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static io.nobt.application.env.Config.Keys.*;
import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ConfigTest {

    private static final String VALID_CONNECTION_STRING = "jdbc:postgresql://postgres:password@postgres:5432/postgres";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "parseExamples")
    public void shouldParseValuesFromEnvironment(Environment environment, Config expected) throws Exception {

        final Config config = Config.from(environment);

        assertThat(config, equalTo(expected));
    }

    private static Object[] parseExamples() {
        return $(
                $(
                        staticEnvironment("8080", "false", "true", VALID_CONNECTION_STRING),
                        new Config(8080, false, true, ConnectionStringAdapter.parse(VALID_CONNECTION_STRING))
                ),
                $(
                        staticEnvironment("8080", "true", "false", unset()),
                        new Config(8080, true, false, null)
                )
        );
    }

    @Test
    public void portShouldBeMandatory() throws Exception {
        expectedException.expect(MissingConfigurationException.class);

        Config.from(new EmptyEnvironment());
    }

    @Test
    @Parameters(method = "invalidEnvironments")
    public void testInvalidConfigurations(Environment environment) throws Exception {

        expectedException.expect(IllegalConfigurationException.class);

        final Config sut = Config.from(environment);
    }

    private static Object[] invalidEnvironments() {
        return $(
                staticEnvironment(aPort(), unset(), unset(), unset()),

                staticEnvironment(aPort(), "false", "true", unset()),
                staticEnvironment(aPort(), unset(), "true", unset()),
                staticEnvironment(aPort(), "false", unset(), unset()),

                staticEnvironment(aPort(), "true", "true", unset()),
                staticEnvironment(aPort(), "true", unset(), unset())
        );
    }

    @Test
    @Parameters(method = "validEnvironments")
    public void testValidConfigurations(Environment environment) throws Exception {
        final Config sut = Config.from(environment);
    }

    private static Object[] validEnvironments() {
        return $(
                staticEnvironment(aPort(), "true", "false", unset()),
                staticEnvironment(aPort(), "false", unset(), VALID_CONNECTION_STRING),
                staticEnvironment(aPort(), unset(), "true", VALID_CONNECTION_STRING),
                staticEnvironment(aPort(), unset(), unset(), VALID_CONNECTION_STRING)
        );
    }

    private static String unset() {
        return null;
    }

    private static String aPort() {
        return "8080";
    }

    private static StaticEnvironment staticEnvironment(final String port, final String inMemory, final String migrate, final String dbConnection) {
        return new StaticEnvironment(new HashMap<String, String>() {{
            put(PORT.name(), port);
            put(MIGRATE_DATABASE_AT_STARTUP.name(), migrate);
            put(USE_IN_MEMORY_DATABASE.name(), inMemory);
            put(DATABASE_CONNECTION_STRING.name(), dbConnection);
        }});
    }
}