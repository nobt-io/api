package io.nobt.persistence.repository;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.DefaultEntityManagerNobtRepositoryFactory;
import io.nobt.persistence.EntityManagerFactoryProvider;
import io.nobt.persistence.EntityManagerNobtRepository;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.domain.factories.ShareFactory;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import io.nobt.test.persistence.TransactionRule;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.factories.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.factories.PaymentBuilderProvider.aPayment;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.ExpenseMatchers.*;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static io.nobt.test.domain.matchers.PaymentMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class EntityManagerNobtRepositoryIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public TransactionRule transactionRule;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6");

    private static EntityManagerFactory entityManagerFactory;
    private static DefaultEntityManagerNobtRepositoryFactory entityManagerNobtRepositoryFactory;

    private EntityManagerNobtRepository sut;

    @BeforeClass
    public static void setupEnvironment() {

        final DatabaseConfig databaseConfig = new PostgreSQLContainerDatabaseConfig(postgreSQLContainer);

        new MigrationService(databaseConfig).migrate();

        final EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider();

        entityManagerNobtRepositoryFactory = new DefaultEntityManagerNobtRepositoryFactory();
        entityManagerFactory = entityManagerFactoryProvider.create(databaseConfig);
    }

    @AfterClass
    public static void tearDownEnvironment() throws IOException {
        entityManagerFactory.close();
    }

    @Before
    public void setUp() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        sut = entityManagerNobtRepositoryFactory.create(entityManager);
        transactionRule = new TransactionRule(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        sut.close();
    }

    @Test
    public void shouldSaveNobt() throws Exception {

        final String name = "Some name";
        final Person[] explicitParticipants = {thomas, david};

        final Nobt nobtToSave = aNobt()
                .withId(null)
                .withName(name)
                .withParticipants(explicitParticipants)
                .build();

        final NobtId id = save(nobtToSave);

        assumeThat(id, is(notNullValue()));

        final Nobt retrievedNobt = fetch(id);

        assertThat(retrievedNobt, allOf(
                hasName(equalTo(name)),
                hasParticipants(containsInAnyOrder(explicitParticipants))
        ));
    }

    @Test
    public void shouldThrowExceptionForUnknownNobt() throws Exception {

        final NobtId unknownId = new NobtId(1234L);

        expectedException.expect(UnknownNobtException.class);
        fetch(unknownId);
    }

    @Test
    public void shouldPersistExpenseAndAddToNobt() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);
        final LocalDate expenseDate = LocalDate.now();

        final Nobt nobtToSave = aNobt()
                .withId(null)
                .withExpenses(anExpense().withDebtee(thomas).withShares(thomasShare, matthiasShare).happendOn(expenseDate))
                .build();

        final NobtId id = save(nobtToSave);

        final Nobt retrievedNobt = fetch(id);

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
    public void savingAndFetchingResultsInDifferentInstance() throws Exception {

        final Nobt nobtToSave = aNobt().withId(null).build();

        final NobtId id = save(nobtToSave);

        final Nobt fetchedNobt = fetch(id);

        assertThat(nobtToSave == fetchedNobt, is(false));
    }

    @Test
    public void shouldRemoveOrphanExpense() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);
        final LocalDate expenseDate = LocalDate.now();

        final Nobt nobtToSave = aNobt()
                .withId(null)
                .withExpenses(anExpense().withDebtee(thomas).withShares(thomasShare, matthiasShare).happendOn(expenseDate))
                .build();

        final NobtId id = save(nobtToSave);

        final Nobt retrievedNobt = fetch(id);

        final Long idOfFirstExpense = retrievedNobt.getExpenses().stream().findFirst().orElseThrow(IllegalStateException::new).getId();

        retrievedNobt.removeExpense(idOfFirstExpense);

        save(retrievedNobt);


        final Nobt nobtWithoutExpense = fetch(id);

        assertThat(nobtWithoutExpense, hasExpenses(
                iterableWithSize(0)
        ));
    }

    @Test
    public void shouldCorrectlyHandleTimezones() throws Exception {

        final ZonedDateTime firstOf2017 = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(5));

        final Nobt nobtToSave = aNobt()
                .withId(null)
                .onDate(firstOf2017)
                .build();

        final NobtId id = save(nobtToSave);

        final Nobt loadedNobt = fetch(id);
        final ZonedDateTime persistedTimestamp = loadedNobt.getCreatedOn();

        final ZonedDateTime expected = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(5));

        assertThat(persistedTimestamp, is(expected));
    }

    @Test
    public void shouldPersistPayment() throws Exception {

        final Nobt nobt = aNobt()
                .withId(null)
                .withParticipants(thomas, matthias)
                .withPayments(
                        aPayment().withSender(thomas).withRecipient(matthias).withAmount(amount(3L))
                )
                .build();


        final NobtId id = save(nobt);


        final Nobt retrievedNobt = fetch(id);
        assertThat(retrievedNobt, hasPayments(iterableWithSize(greaterThan(0))));
        assertThat(retrievedNobt.getPayments().iterator().next(), allOf(
                hasSender(equalTo(thomas)),
                hasRecipient(equalTo(matthias)),
                hasAmount(equalTo(amount(3)))
        ));
    }

    private NobtId save(Nobt nobtToSave) {
        return sut.save(nobtToSave);
    }

    private Nobt fetch(NobtId id) {
        return sut.getById(id);
    }
}
