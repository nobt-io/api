package io.nobt.core.domain;

import io.nobt.core.validation.CheckNoDuplicateDebtors;
import io.nobt.core.validation.org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ExpenseDraft {

    private final String name;
    private final String splitStrategy;
    private final Person debtee;
    private final List<Share> shares;
    private final LocalDate date;
    private final ConversionInformation conversionInformation;

    public ExpenseDraft(String name, String splitStrategy, Person debtee, List<Share> shares, LocalDate date, ConversionInformation conversionInformation) {
        this.name = name;
        this.splitStrategy = splitStrategy;
        this.debtee = debtee;
        this.shares = shares;
        this.date = date;
        this.conversionInformation = conversionInformation;
    }

    @NotEmpty
    public String getName() {
        return name;
    }

    @NotEmpty
    public String getSplitStrategy() {
        return splitStrategy;
    }

    @Valid
    @NotNull
    public Person getDebtee() {
        return debtee;
    }

    @Valid
    @NotEmpty
    @CheckNoDuplicateDebtors
    public List<Share> getShares() {
        return shares;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    @Valid
    public Optional<ConversionInformation> getConversionInformation() {
        return Optional.ofNullable(conversionInformation);
    }
}
