package io.nobt.core.domain;

import java.util.Objects;

public final class Share {

    private final Person debtor;
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
