package io.nobt.persistence.repository;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.*;
import io.nobt.persistence.*;
import io.nobt.persistence.mapping.EntityManagerNobtDatabaseIdResolver;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.domain.factories.ShareFactory;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import io.nobt.util.Sets;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManagerFactory;
import java.time.*;
import java.util.Collections;

import static io.nobt.test.domain.Currencies.EUR;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.ExpenseMatchers.*;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class NobtRepositoryIT {

    private static DatabaseConfig databaseConfig;
    private static MigrationService migrationService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6");

    private static EntityManagerFactory entityManagerFactory;

    private NobtFactory nobtFactory;

    private TransactionalNobtRepositoryCommandInvoker commandInvoker;

    @BeforeClass
    public static void setupEnvironment() {

        databaseConfig = new PostgreSQLContainerDatabaseConfig(postgreSQLContainer);

        migrationService = new MigrationService(databaseConfig);
        migrationService.migrate();
    }

    @AfterClass
    public static void cleanupEnvironment() {
        migrationService.clean();
    }

    @Before
    public void setUp() throws Exception {

        final EntityManagerFactoryProvider emfProvider = new EntityManagerFactoryProvider();

        entityManagerFactory = emfProvider.create(databaseConfig);

        commandInvoker = new TransactionalNobtRepositoryCommandInvoker(entityManagerFactory, (entityManager -> {

            final ShareMapper shareMapper = new ShareMapper();
            final ExpenseMapper expenseMapper = new ExpenseMapper(shareMapper);
            final NobtMapper nobtMapper = new NobtMapper(new EntityManagerNobtDatabaseIdResolver(entityManager), expenseMapper);

            return new EntityManagerNobtRepository(entityManager, nobtMapper);
        }));

        nobtFactory = new NobtFactory();
    }

    @After
    public void closeEM() {
        entityManagerFactory.close();
    }

    @Test
    public void shouldSaveNobt() throws Exception {

        final String name = "Some name";
        final Person[] explicitParticipants = {thomas, david};

        final Nobt nobtToSave = nobtFactory.create(name, Sets.newHashSet(explicitParticipants), new CurrencyKey("EUR"));

        final NobtId id = saveNobt(nobtToSave);

        assumeThat(id, is(notNullValue()));

        final Nobt retrievedNobt = getNobt(id);

        assertThat(retrievedNobt, allOf(
                hasName(equalTo(name)),
                hasParticipants(containsInAnyOrder(explicitParticipants))
        ));
    }

    @Test
    public void shouldThrowExceptionForUnknownNobt() throws Exception {

        final NobtId unknownId = new NobtId("abcd");

        expectedException.expect(UnknownNobtException.class);
        getNobt(unknownId);
    }

    @Test
    public void shouldPersistExpenseAndAddToNobt() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);
        final LocalDate expenseDate = LocalDate.now();

        final Nobt nobtToSave = nobtFactory.create("Some name", Collections.emptySet(), new CurrencyKey("EUR"));
        nobtToSave.addExpense("Billa", "UNKNOWN", thomas, Sets.newHashSet(thomasShare, matthiasShare), expenseDate, null);

        final NobtId id = saveNobt(nobtToSave);

        final Nobt retrievedNobt = getNobt(id);

        assertThat(retrievedNobt, hasExpenses(
                allOf(
                        iterableWithSize(1),
                        hasItem(
                                allOf(
                                        hasDebtee(equalTo(thomas)),
                                        hasShares(containsInAnyOrder(thomasShare, matthiasShare)),
                                        onDate(equalTo(expenseDate))
                                )
                        )
                ))
        );
    }

    @Test
    public void shouldRemoveOrphanExpense() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);
        final LocalDate expenseDate = LocalDate.now();

        final Nobt nobtToSave = nobtFactory.create("Some name", Collections.emptySet(), new CurrencyKey("EUR"));
        nobtToSave.addExpense("Billa", "UNKNOWN", thomas, Sets.newHashSet(thomasShare, matthiasShare), expenseDate, null);

        final NobtId id = saveNobt(nobtToSave);

        final Nobt retrievedNobt = getNobt(id);

        final Long idOfFirstExpense = retrievedNobt.getExpenses().stream().findFirst().orElseThrow(IllegalStateException::new).getId();

        retrievedNobt.removeExpense(idOfFirstExpense);

        saveNobt(retrievedNobt);


        final Nobt nobtWithoutExpense = getNobt(id);

        assertThat(nobtWithoutExpense, hasExpenses(
                iterableWithSize(0)
        ));
    }

    @Test
    public void shouldCorrectlyHandleTimezones() throws Exception {

        final LocalDateTime firstOf2017 = LocalDateTime.of(2017, 1, 1, 0, 0, 0, 0);
        final ZoneOffset fiveHourOffset = ZoneOffset.ofHours(5);

        final NobtFactory nobtFactory = new NobtFactory(Clock.fixed(firstOf2017.toInstant(fiveHourOffset), fiveHourOffset));

        final Nobt nobtToSave = nobtFactory.create("Test", Collections.emptySet(), EUR);

        final NobtId id = saveNobt(nobtToSave);

        final Nobt loadedNobt = getNobt(id);
        final ZonedDateTime persistedTimestamp = loadedNobt.getCreatedOn().withZoneSameInstant(fiveHourOffset);

        final ZonedDateTime expected = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, fiveHourOffset);

        assertThat(persistedTimestamp, is(expected));
    }

    private NobtId saveNobt(Nobt nobtToSave) {
        return commandInvoker.invoke(repository -> repository.save(nobtToSave));
    }

    private Nobt getNobt(NobtId id) {
        return commandInvoker.invoke(repository -> repository.getById(id));
    }
}
