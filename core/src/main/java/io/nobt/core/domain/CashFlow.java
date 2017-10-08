package io.nobt.core.domain;

import io.nobt.core.domain.transaction.Debt;

import java.time.ZonedDateTime;
import java.util.Set;

public interface CashFlow {

    long getId();

    Set<Debt> calculateAccruingDebts();

    ZonedDateTime getCreatedOn();

}
