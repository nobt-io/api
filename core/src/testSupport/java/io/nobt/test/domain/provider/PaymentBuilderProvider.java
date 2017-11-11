package io.nobt.test.domain.provider;

import io.nobt.core.domain.ConversionInformation;
import io.nobt.core.domain.CurrencyKey;
import io.nobt.test.domain.builder.PaymentBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static io.nobt.test.domain.provider.IDProvider.nextId;

public final class PaymentBuilderProvider {

    private PaymentBuilderProvider() {
    }

    public static PaymentBuilder aPayment() {
        return new PaymentBuilder()
                .withId(nextId())
                .withDescription(UUID.randomUUID().toString())
                .withConversionInformation(new ConversionInformation(new CurrencyKey("EUR"), BigDecimal.ONE))
                .happendOn(LocalDate.now().minusDays(1))
                .createdOn(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
