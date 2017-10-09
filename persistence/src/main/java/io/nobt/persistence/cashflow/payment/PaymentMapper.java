package io.nobt.persistence.cashflow.payment;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Payment;
import io.nobt.core.domain.Person;
import io.nobt.persistence.DomainModelMapper;
import io.nobt.persistence.cashflow.CashFlowEntity;

public class PaymentMapper implements DomainModelMapper<PaymentEntity, Payment> {

    @Override
    public Payment mapToDomainModel(PaymentEntity databaseModel) {
        return new Payment(
                databaseModel.getKey().getId(),
                Person.forName(databaseModel.getSender()),
                Person.forName(databaseModel.getRecipient()),
                Amount.fromBigDecimal(databaseModel.getAmount()),
                databaseModel.getDescription(),
                databaseModel.getCreatedOn()
        );
    }

    @Override
    public PaymentEntity mapToDatabaseModel(Payment domainModel) {

        final PaymentEntity entity = new PaymentEntity();
        entity.setKey(new CashFlowEntity.Key(domainModel.getId(), null));
        entity.setSender(domainModel.getSender().getName());
        entity.setRecipient(domainModel.getRecipient().getName());
        entity.setAmount(domainModel.getAmount().getRoundedValue());
        entity.setDescription(domainModel.getDescription());
        entity.setCreatedOn(domainModel.getCreatedOn());

        return entity;
    }
}
