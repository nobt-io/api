package io.nobt.test.domain.factories;

import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import java.util.Random;

public final class ShareFactory {

    private static final Random amountValueProvider = new Random();

    public static Share randomShare(Person person) {
        return new Share(person, Amount.fromDouble(amountValueProvider.nextDouble()));
    }

    public static Share share(Person person, double amount) {
        return share(person, AmountFactory.amount(amount));
    }

    public static Share share(Person person, Amount amount) {
        return new Share(person, amount);
    }
}