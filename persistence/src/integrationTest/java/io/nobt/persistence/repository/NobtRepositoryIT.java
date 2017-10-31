package io.nobt.persistence.repository;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.*;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.EntityManagerFactoryProvider;
import io.nobt.persistence.NobtRepository;
import io.nobt.persistence.NobtRepositoryImpl;
import io.nobt.persistence.cashflow.expense.ExpenseMapper;
import io.nobt.persistence.cashflow.payment.PaymentMapper;
import io.nobt.persistence.nobt.NobtMapper;
import io.nobt.persistence.share.ShareMapper;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.domain.factories.ShareFactory;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import io.nobt.util.Sets;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;

import static io.nobt.test.domain.Currencies.EUR;
import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.ExpenseMatchers.*;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static io.nobt.test.domain.matchers.PaymentMatchers.*;
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
    private static EntityManager entityManager;

    private NobtFactory nobtFactory;

    private NobtRepository sut;

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
        entityManager = entityManagerFactory.createEntityManager();

        final ShareMapper shareMapper = new ShareMapper();
        final ExpenseMapper expenseMapper = new ExpenseMapper(shareMapper);
        final NobtMapper nobtMapper = new NobtMapper(expenseMapper, new PaymentMapper());

        sut = new NobtRepositoryImpl(entityManager, nobtMapper);

        nobtFactory = new NobtFactory();
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

        final Nobt nobtToSave = nobtFactory.create(name, Sets.newHashSet(explicitParticipants), new CurrencyKey("EUR"));

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
        final LocalDate expenseDate = LocalDate.now();

        final Nobt nobtToSave = nobtFactory.create("Some name", Collections.emptySet(), new CurrencyKey("EUR"));
        nobtToSave.addExpense("Billa", "UNKNOWN", thomas, Sets.newHashSet(thomasShare, matthiasShare), expenseDate, null);

        final NobtId id = sut.save(nobtToSave);

        final Nobt retrievedNobt = sut.getById(id);

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

        final NobtId id = sut.save(nobtToSave);


        final Nobt retrievedNobt = sut.getById(id);
        final Long idOfFirstExpense = retrievedNobt.getExpenses().stream().findFirst().orElseThrow(IllegalStateException::new).getId();

        retrievedNobt.removeExpense(idOfFirstExpense);

        sut.save(retrievedNobt);


        final Nobt nobtWithoutExpense = sut.getById(id);

        assertThat(nobtWithoutExpense, hasExpenses(
                iterableWithSize(0)
        ));
    }

    @Test
    public void shouldCorrectlyHandleTimezones() throws Exception {

        final ZonedDateTime firstOf2017 = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(5));

        final Nobt nobtToSave = new Nobt(null, EUR, "Test", Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), firstOf2017, null);

        final NobtId id = sut.save(nobtToSave);

        final Nobt loadedNobt = sut.getById(id);
        final ZonedDateTime persistedTimestamp = loadedNobt.getCreatedOn();

        final ZonedDateTime expected = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(5));

        assertThat(persistedTimestamp, is(expected));
    }

    @Test
    public void shouldPersistPayment() throws Exception {
        final Nobt nobt = nobtFactory.create("Test", Sets.newHashSet(thomas, matthias), new CurrencyKey("EUR"));
        nobt.addPayment(thomas, amount(3L), matthias, "Testzahlung");


        final NobtId id = sut.save(nobt);


        final Nobt retrievedNobt = sut.getById(id);
        assertThat(retrievedNobt, hasPayments(iterableWithSize(greaterThan(0))));
        assertThat(retrievedNobt.getPayments().iterator().next(), allOf(
                hasSender(equalTo(thomas)),
                hasRecipient(equalTo(matthias)),
                hasAmount(equalTo(amount(3)))
        ));
    }
}
