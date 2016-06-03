package io.nobt.persistence;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.service.ServiceRegistry;

/**
 * http://stackoverflow.com/questions/1007176/using-different-hibernate-user-types-in-different-situations
 */
public class UUIDAwareH2Dialect extends HSQLDialect {

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);

        typeContributions.contributeType(new UUIDStringCustomType());
    }
}