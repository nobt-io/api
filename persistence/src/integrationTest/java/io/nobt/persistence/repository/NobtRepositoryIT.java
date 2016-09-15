package io.nobt.persistence.repository;

import io.nobt.application.env.Config;
import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.EntityManagerFactoryProvider;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryImpl;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.domain.factories.ShareFactory;
import io.nobt.test.persistence.DatabaseAvailabilityCheck;
import io.nobt.util.Sets;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;

import static io.nobt.application.env.Config.Keys.DATABASE_CONNECTION_STRING;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.ExpenseMatchers.hasDebtee;
import static io.nobt.test.domain.matchers.ExpenseMatchers.hasShares;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class NobtRepositoryIT {

    private static DatabaseConfig databaseConfig;
    private static MigrationService migrationService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    private NobtRepository sut;

    @BeforeClass
    public static void setupEnvironment() {

        databaseConfig = Config.database().orElseThrow(missingConfigurationException(DATABASE_CONNECTION_STRING));
        migrationService = new MigrationService(databaseConfig);

        DatabaseAvailabilityCheck availabilityCheck = new DatabaseAvailabilityCheck(databaseConfig);
        await().until(availabilityCheck::isDatabaseUp);

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
        entityManager = entityManagerFactory.createEntityManager();

        final ShareMapper shareMapper = new ShareMapper();
        final ExpenseMapper expenseMapper = new ExpenseMapper(shareMapper);
        final NobtMapper nobtMapper = new NobtMapper(expenseMapper);

        sut = new NobtRepositoryImpl(entityManager, nobtMapper);
    }

    @After
    public void closeEM() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldSaveNobt() throws Exception {

        final String name = "Some name";
        final Person[] explicitParticipants = {thomas, david};

        final Nobt nobtToSave = new Nobt(null, name, Sets.newHashSet(explicitParticipants), Collections.emptySet());

        final NobtId id = sut.save(nobtToSave);

        assumeThat(id, is(notNullValue()));

        final Nobt retrievedNobt = sut.getById(id);

        assertThat(retrievedNobt, allOf(
                hasName(equalTo(name)),
                hasParticipants(containsInAnyOrder(explicitParticipants))
        ));
    }

    @Test
    public void shouldThrowExceptionForUnknownNobt() throws Exception {

        final NobtId unknownId = new NobtId(1234L);

        expectedException.expect(UnknownNobtException.class);
        sut.getById(unknownId);
    }

    @Test
    public void shouldPersistExpenseAndAddToNobt() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);

        final Nobt nobtToSave = new Nobt(null, "Some name", Collections.emptySet(), Collections.emptySet());
        nobtToSave.addExpense("Billa", "UNKNOWN", thomas, Sets.newHashSet(thomasShare, matthiasShare));

        final NobtId id = sut.save(nobtToSave);

        final Nobt retrievedNobt = sut.getById(id);

        assertThat(retrievedNobt, hasExpenses(
                allOf(
                        iterableWithSize(1),
                        hasItem(
                                allOf(
                                        hasDebtee(equalTo(thomas)),
                                        hasShares(containsInAnyOrder(thomasShare, matthiasShare))
                                )
                        )
                ))
        );
    }
}
