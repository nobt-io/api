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
public class ConfigBuilderTest {

    private static final String VALID_CONNECTION_STRING = "jdbc:postgresql://postgres:password@postgres:5432/postgres";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    @Parameters(method = "parseExamples")
    public void shouldParseValuesFromEnvironment(Environment environment, Config expected) throws Exception {

        final Config config = ConfigBuilder.newInstance().applyEnvironment(environment).build();

        assertThat(config, equalTo(expected));
    }

    private static Object[] parseExamples() {
        return $(
                $(
                        staticEnvironment("8080", "false", "true", VALID_CONNECTION_STRING, "X-Custom-Header"),
                        new Config(8080, false, true, ConnectionStringAdapter.parse(VALID_CONNECTION_STRING), "X-Custom-Header")
                ),
                $(
                        staticEnvironment("8080", "true", "false", unset(), unset()),
                        new Config(8080, true, false, null, null)
                )
        );
    }

    @Test
    public void portShouldBeMandatory() throws Exception {
        expectedException.expect(MissingConfigurationException.class);

        ConfigBuilder.newInstance().applyEnvironment(new EmptyEnvironment()).build();
    }

    @Test
    @Parameters(method = "invalidEnvironments")
    public void testInvalidConfigurations(Environment environment) throws Exception {

        expectedException.expect(IllegalConfigurationException.class);

        final Config sut = ConfigBuilder.newInstance().applyEnvironment(environment).build();
    }

    private static Object[] invalidEnvironments() {
        return $(
                staticEnvironment(aPort(), unset(), unset(), unset(), "X-Custom-Header"),

                staticEnvironment(aPort(), "false", "true", unset(), "X-Custom-Header"),
                staticEnvironment(aPort(), unset(), "true", unset(), "X-Custom-Header"),
                staticEnvironment(aPort(), "false", unset(), unset(), "X-Custom-Header"),

                staticEnvironment(aPort(), "true", "true", unset(), "X-Custom-Header"),
                staticEnvironment(aPort(), "true", unset(), unset(), "X-Custom-Header"),

                staticEnvironment(aPort(), "false", "false", unset(), "X-Custom-Header")
        );
    }

    @Test
    @Parameters(method = "validEnvironments")
    public void testValidConfigurations(Environment environment) throws Exception {
        final Config sut = ConfigBuilder.newInstance().applyEnvironment(environment).build();
    }

    private static Object[] validEnvironments() {
        return $(
                staticEnvironment(aPort(), "true", "false", unset(), "X-Custom-Header"),
                staticEnvironment(aPort(), "false", unset(), VALID_CONNECTION_STRING, "X-Custom-Header"),
                staticEnvironment(aPort(), unset(), "true", VALID_CONNECTION_STRING, "X-Custom-Header"),
                staticEnvironment(aPort(), unset(), unset(), VALID_CONNECTION_STRING, "X-Custom-Header")
        );
    }

    private static String unset() {
        return null;
    }

    private static String aPort() {
        return "8080";
    }

    private static StaticEnvironment staticEnvironment(final String port, final String inMemory, final String migrate, final String dbConnection, String schemeOverrideHeader) {
        return new StaticEnvironment(new HashMap<String, String>() {{
            put(PORT.name(), port);
            put(MIGRATE_DATABASE_AT_STARTUP.name(), migrate);
            put(USE_IN_MEMORY_DATABASE.name(), inMemory);
            put(DATABASE_CONNECTION_STRING.name(), dbConnection);
            put(SCHEME_OVERRIDE_HEADER.name(), schemeOverrideHeader);
        }});
    }
}