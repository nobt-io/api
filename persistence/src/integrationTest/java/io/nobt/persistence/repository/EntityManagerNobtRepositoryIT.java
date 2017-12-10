package io.nobt.persistence.repository;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.commands.RetrieveNobtCommand;
import io.nobt.core.domain.Nobt;
import io.nobt.core.domain.NobtId;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.DatabaseConfig;
import io.nobt.persistence.DefaultEntityManagerNobtRepositoryFactory;
import io.nobt.persistence.EntityManagerFactoryProvider;
import io.nobt.persistence.TransactionalNobtRepositoryCommandInvoker;
import io.nobt.sql.flyway.MigrationService;
import io.nobt.test.domain.factories.ShareFactory;
import io.nobt.test.persistence.PostgreSQLContainerDatabaseConfig;
import io.nobt.test.persistence.TransactionRule;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static io.nobt.test.domain.factories.AmountFactory.amount;
import static io.nobt.test.domain.factories.StaticPersonFactory.*;
import static io.nobt.test.domain.matchers.ExpenseMatchers.*;
import static io.nobt.test.domain.matchers.NobtMatchers.*;
import static io.nobt.test.domain.matchers.PaymentMatchers.*;
import static io.nobt.test.domain.provider.ExpenseBuilderProvider.anExpense;
import static io.nobt.test.domain.provider.NobtBuilderProvider.aNobt;
import static io.nobt.test.domain.provider.PaymentBuilderProvider.aPayment;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class EntityManagerNobtRepositoryIT {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:9.6");
    private static TransactionalNobtRepositoryCommandInvoker invoker;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Rule
    public TransactionRule transactionRule;

    @BeforeClass
    public static void setupEnvironment() {

        final DatabaseConfig databaseConfig = new PostgreSQLContainerDatabaseConfig(postgreSQLContainer);

        new MigrationService(databaseConfig).migrate();

        final EntityManagerFactoryProvider entityManagerFactoryProvider = new EntityManagerFactoryProvider();
        final EntityManagerFactory entityManagerFactory = entityManagerFactoryProvider.create(databaseConfig);

        invoker = new TransactionalNobtRepositoryCommandInvoker(entityManagerFactory, new DefaultEntityManagerNobtRepositoryFactory());
    }

    @AfterClass
    public static void tearDownEnvironment() throws IOException {
        invoker.close();
    }

    @Test
    public void shouldSaveNobt() throws Exception {

        final String name = "Some name";
        final Person[] explicitParticipants = {thomas, david};

        final Nobt nobtToSave = aNobt()
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

        final NobtId unknownId = new NobtId("foobar");

        expectedException.expect(UnknownNobtException.class);
        fetch(unknownId);
    }

    @Test
    public void shouldPersistExpenseAndAddToNobt() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);
        final LocalDate expenseDate = LocalDate.now();

        final Nobt nobtToSave = aNobt()
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

        final Nobt nobtToSave = aNobt().build();

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
                .onDate(firstOf2017)
                .build();

        final NobtId id = save(nobtToSave);

        final Nobt loadedNobt = fetch(id);
        final Instant persistedTimestamp = loadedNobt.getCreatedOn().toInstant();

        assertThat(persistedTimestamp, is(firstOf2017.toInstant()));
    }

    @Test
    public void shouldPersistPayment() throws Exception {

        final Nobt nobt = aNobt()
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
        return invoker.invoke(repository -> repository.save(nobtToSave));
    }

    private Nobt fetch(NobtId id) {
        return invoker.invoke(new RetrieveNobtCommand(id));
    }
}
