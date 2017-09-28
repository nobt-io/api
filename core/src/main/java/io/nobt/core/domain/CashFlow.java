package io.nobt.core.domain;

import java.time.ZonedDateTime;
import java.util.Set;

public interface CashFlow {

    Set<Debt> calculateAccruingDebts();

    ZonedDateTime getCreatedOn();

}
