package io.nobt.core.domain;

import io.nobt.core.validation.org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public class ExpenseDraft {

    @NotEmpty
    private final String name;

    @NotEmpty
    private final String splitStrategy;

    @Valid
    @NotNull
    private final Person debtee;

    @Valid
    @NotEmpty
    private final Set<Share> shares;

    @NotNull
    private final LocalDate date;

    @Valid
    @NotNull
    private final ConversionInformation conversionInformation;

    public ExpenseDraft(String name, String splitStrategy, Person debtee, Set<Share> shares, LocalDate date, ConversionInformation conversionInformation) {
        this.name = name;
        this.splitStrategy = splitStrategy;
        this.debtee = debtee;
        this.shares = shares;
        this.date = date;
        this.conversionInformation = conversionInformation;
    }

    public String getName() {
        return name;
    }

    public String getSplitStrategy() {
        return splitStrategy;
    }

    public Person getDebtee() {
        return debtee;
    }

    public Set<Share> getShares() {
        return shares;
    }

    public LocalDate getDate() {
        return date;
    }

    public Optional<ConversionInformation> getConversionInformation() {
        return Optional.ofNullable(conversionInformation);
    }

}
