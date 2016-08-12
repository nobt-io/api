package io.nobt.persistence.mapping.bson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;

public abstract class ShareMixin {

    @JsonCreator
    public ShareMixin(@JsonProperty("debtor") Person debtor, @JsonProperty("amount") Amount amount) {

    }
}
