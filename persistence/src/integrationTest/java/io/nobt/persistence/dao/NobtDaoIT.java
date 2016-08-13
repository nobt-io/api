package io.nobt.persistence.dao;

import io.nobt.core.UnknownNobtException;
import io.nobt.core.domain.*;
import io.nobt.core.domain.test.ShareFactory;
import io.nobt.persistence.NobtDao;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;
import io.nobt.util.Sets;
import org.hibernate.HibernateException;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;

import static io.nobt.core.domain.test.StaticPersonFactory.*;
import static io.nobt.matchers.ExpenseMatchers.hasDebtee;
import static io.nobt.matchers.ExpenseMatchers.hasShares;
import static io.nobt.matchers.NobtMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class NobtDaoIT {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    private NobtDao sut;

    @BeforeClass
    public static void initializeEM() throws HibernateException {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence-test");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Before
    public void setUp() throws Exception {
        final ShareMapper shareMapper = new ShareMapper();
        final ExpenseMapper expenseMapper = new ExpenseMapper(shareMapper);
        final NobtMapper nobtMapper = new NobtMapper(expenseMapper);

        sut = new NobtDaoImpl(entityManager, nobtMapper, expenseMapper, shareMapper);
    }

    @AfterClass
    public static void closeEM() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void shouldPersistAndRetrieveNobt() throws Exception {

        final String name = "Some name";
        final Person[] explicitParticipants = {thomas, david};

        final Nobt nobt = sut.createNobt(name, Sets.newHashSet(explicitParticipants));

        assumeThat(nobt, allOf(
                hasName(equalTo(name)),
                hasParticipants(containsInAnyOrder(explicitParticipants))
        ));


        final Nobt retrievedNobt = sut.get(nobt.getId());

        assertThat(retrievedNobt, allOf(
                hasName(equalTo(name)),
                hasParticipants(containsInAnyOrder(explicitParticipants))
        ));
    }

    @Test
    public void shouldThrowExceptionForUnknownNobt() throws Exception {

        final NobtId unknownId = new NobtId(1234L);

        expectedException.expect(UnknownNobtException.class);
        sut.get(unknownId);
    }

    @Test
    public void shouldPersistExpenseAndAddToNobt() throws Exception {

        final Share thomasShare = ShareFactory.randomShare(thomas);
        final Share matthiasShare = ShareFactory.randomShare(matthias);

        final Nobt nobt = sut.createNobt("Some name", Collections.emptySet());

        final Expense expense = sut.createExpense(nobt.getId(), "Billa", "UNKNOWN", thomas, Sets.newHashSet(thomasShare, matthiasShare));

        // early fail if expense cannot be properly persisted
        assumeThat(expense, allOf(
                hasDebtee(equalTo(thomas)),
                hasShares(containsInAnyOrder(thomasShare, matthiasShare))
                )
        );


        final Nobt retrievedNobt = sut.get(nobt.getId());

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
