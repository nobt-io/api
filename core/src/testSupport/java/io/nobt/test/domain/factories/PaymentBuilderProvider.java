package io.nobt.test.domain.factories;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static io.nobt.test.domain.factories.IDProvider.nextId;

public final class PaymentBuilderProvider {

    private PaymentBuilderProvider() {
    }

    public static PaymentBuilder aPayment() {
        return new PaymentBuilder()
                .withId(nextId())
                .withDescription(UUID.randomUUID().toString())
                .happendOn(LocalDate.now().minusDays(1))
                .createdOn(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
