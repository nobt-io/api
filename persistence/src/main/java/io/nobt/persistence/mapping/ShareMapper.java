package io.nobt.persistence.mapping;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;
import io.nobt.persistence.entity.ShareEntity;

public class ShareMapper implements DomainModelMapper<ShareEntity, Share> {

    @Override
    public Share mapToDomainModel(ShareEntity databaseModel) {

        final Person debtor = Person.forName(databaseModel.getDebtor());
        final Amount amount = Amount.fromBigDecimal(databaseModel.getAmount());

        return new Share(debtor, amount);
    }

    @Override
    public ShareEntity mapToDatabaseModel(Share domainModel) {

        final ShareEntity shareEntity = new ShareEntity();

        shareEntity.setAmount(domainModel.getAmount().getRoundedValue());
        shareEntity.setDebtor(domainModel.getDebtor().getName());

        return shareEntity;
    }
}
