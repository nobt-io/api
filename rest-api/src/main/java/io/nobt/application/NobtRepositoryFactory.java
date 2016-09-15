package io.nobt.application;

import io.nobt.application.env.Config;
import io.nobt.application.env.MissingConfigurationException;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Share;
import io.nobt.persistence.*;
import io.nobt.persistence.entity.ExpenseEntity;
import io.nobt.persistence.entity.ShareEntity;
import io.nobt.persistence.mapping.DomainModelMapper;
import io.nobt.persistence.mapping.ExpenseMapper;
import io.nobt.persistence.mapping.NobtMapper;
import io.nobt.persistence.mapping.ShareMapper;

import javax.persistence.EntityManager;

import static io.nobt.application.env.Config.Keys.DATABASE_CONNECTION_STRING;
import static io.nobt.application.env.Config.Keys.PORT;
import static io.nobt.application.env.MissingConfigurationException.missingConfigurationException;

public class NobtRepositoryFactory {

    public NobtRepository create() {

        // make sure that, if unsure we always try to connect to a real database
        if (Config.useInMemoryDatabase().orElse(false)) {
            return new InMemoryNobtRepository();
        } else {

            final DatabaseConfig config = Config.database().orElseThrow(missingConfigurationException(DATABASE_CONNECTION_STRING));

            final EntityManager entityManager = new EntityManagerFactoryProvider().create(config).createEntityManager();

            final DomainModelMapper<ShareEntity, Share> shareMapper = new ShareMapper();
            final DomainModelMapper<ExpenseEntity, Expense> expenseMapper = new ExpenseMapper(shareMapper);

            return new NobtRepositoryImpl(entityManager, new NobtMapper(expenseMapper));
        }
    }
}
