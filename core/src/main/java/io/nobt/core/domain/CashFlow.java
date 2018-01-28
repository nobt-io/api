package io.nobt.core.domain;

import io.nobt.core.domain.debt.Debt;

import java.time.Instant;
import java.util.Set;

public interface CashFlow {

    long getId();

    Set<Debt> calculateAccruingDebts();

    Instant getCreatedOn();

    Set<Person> getParticipants();
}
