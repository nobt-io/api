package io.nobt.core.domain;

import io.nobt.core.validation.Positive;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public final class Share {

    @Valid
    @NotNull
    private final Person debtor;

    @Positive
    private final Amount amount;

    public Share(Person debtor, Amount amount) {
        this.debtor = debtor;
        this.amount = amount;
    }

    public Person getDebtor() {
        return debtor;
    }

    public Amount getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Share)) {
            return false;
        }
        Share share = (Share) o;
        return Objects.equals(debtor, share.debtor) &&
                Objects.equals(amount, share.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debtor, amount);
    }

    @Override
    public String toString() {
        return String.format("Share{debtor=%s, amount=%s}", debtor, amount);
    }
}
