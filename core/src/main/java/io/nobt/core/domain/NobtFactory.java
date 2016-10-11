package io.nobt.core.domain;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static java.util.Collections.emptySet;

public final class NobtFactory {

    public Nobt create(String name, Set<Person> explicitParticipants, CurrencyKey currencyKey) {
        return new Nobt(null, currencyKey, name, explicitParticipants, emptySet(), LocalDateTime.now(ZoneOffset.UTC));
    }
}
