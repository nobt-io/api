package io.nobt.persistence.mapping.bson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

public class BsonDomainModule extends SimpleModule {

    public BsonDomainModule() {
        addSerializer(Person.class, new BsonPersonSerializer());
        addDeserializer(Person.class, new BsonPersonDeserializer());

        addSerializer(Amount.class, new BsonAmountSerializer());
        addDeserializer(Amount.class, new BsonAmountDeserializer());

        setMixInAnnotation(Share.class, ShareMixin.class);
    }
}
