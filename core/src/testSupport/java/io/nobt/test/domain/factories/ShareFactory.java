package io.nobt.test.domain.factories;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import static io.nobt.test.domain.factories.AmountFactory.randomAmount;

public final class ShareFactory {

    public static Share randomShare(Person person) {
        return new Share(person, randomAmount());
    }

    public static Share share(Person person, double amount) {
        return new Share(person, Amount.fromDouble(amount));
    }
}