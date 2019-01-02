package io.nobt.core.optimizer;

import io.nobt.core.domain.Person;
import io.nobt.core.domain.debt.Debt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class MinimalNumberOfDebtsOptimizer {

    private static final Logger LOGGER = LogManager.getLogger();

    private List<Debt> debts;
    private boolean needsFurtherOptimization;

    public MinimalNumberOfDebtsOptimizer(List<Debt> debts) {
        this.debts = new ArrayList<>(debts);
    }

    public List<Debt> getMinimalNumberOfDebts() {
        do {
            debts = optimize(debts);
        } while (needsFurtherOptimization);

        return debts;
    }

    private List<Debt> optimize(List<Debt> debts) {

        needsFurtherOptimization = false;

        LOGGER.trace("Trying to minimize number of debts for {}", debts);

        final List<Debt> smallToLarge = debts.stream().sorted(comparing(Debt::getRoundedAmount)).collect(toList());

        for (Debt candidate : smallToLarge) {

            LOGGER.trace("Next candidate is: {}", candidate);

            final Person smallestDebtDebtor = candidate.getDebtor();
            final List<Person> alternativeDebtees = findAlternativeDebtees(debts, smallestDebtDebtor, candidate.getDebtee());

            LOGGER.trace("Alternative debtees: {}", alternativeDebtees);

            for (Person alternativeDebtee : alternativeDebtees) {
                final List<Person> alternativeDebtors = findAlternativeDebtors(debts, smallestDebtDebtor, alternativeDebtee);

                LOGGER.trace("Given alternative debtee {}, alternative debtors are {}", alternativeDebtee, alternativeDebtors);

                for (Person alternativeDebtor : alternativeDebtors) {
                    final Optional<AlternativeDebts> maybeAlternativeDebts = findAlternativeDebts(debts, candidate, alternativeDebtor, alternativeDebtee);

                    if (maybeAlternativeDebts.isPresent()) {

                        final AlternativeDebts alternativeDebts = maybeAlternativeDebts.get();

                        LOGGER.trace("Found alternative way to cover {}", candidate);

                        final ArrayList<Debt> newDebts = new ArrayList<>(debts);
                        newDebts.remove(candidate);

                        final Debt toOriginalDebtee = alternativeDebts.toOriginalDebtee;
                        newDebts.remove(toOriginalDebtee);
                        newDebts.add(toOriginalDebtee.withNewAmount(toOriginalDebtee.getAmount().plus(candidate.getAmount())));

                        LOGGER.trace("{} pays an additional {} to {}", toOriginalDebtee.getDebtor(), candidate.getRoundedAmount(), toOriginalDebtee.getDebtee());

                        final Debt fromOriginalDebtor = alternativeDebts.fromOriginalDebtor;
                        newDebts.remove(fromOriginalDebtor);
                        newDebts.add(fromOriginalDebtor.withNewAmount(fromOriginalDebtor.getAmount().plus(candidate.getAmount())));

                        LOGGER.trace("{} pays an additional {} to {}", fromOriginalDebtor.getDebtor(), candidate.getRoundedAmount(), fromOriginalDebtor.getDebtee());

                        final Debt compensationDebt = alternativeDebts.compensationDebt;
                        newDebts.remove(compensationDebt);
                        newDebts.add(compensationDebt.withNewAmount(compensationDebt.getAmount().minus(candidate.getAmount())));

                        LOGGER.trace("{} now has to pay {} less to {}", compensationDebt.getDebtor(), candidate.getRoundedAmount(), compensationDebt.getDebtee());

                        needsFurtherOptimization = true;

                        return newDebts;
                    }
                }
            }

            LOGGER.trace("{} cannot be covered in a different way.", candidate);
        }

        return debts;
    }

    private static List<Person> findAlternativeDebtees(List<Debt> debts, Person originalDebtor, Person originalDebtee) {
        return debts
                .stream()
                .filter(debt -> {
                    final boolean isSameDebtor = debt.getDebtor().equals(originalDebtor);
                    final boolean isSameDebtee = debt.getDebtee().equals(originalDebtee);

                    return isSameDebtor && !isSameDebtee;
                })
                .map(Debt::getDebtee)
                .collect(toList());
    }

    private static List<Person> findAlternativeDebtors(List<Debt> debts, Person originalDebtor, Person alternativeDebtee) {
        return debts
                .stream()
                .filter(debt -> {
                    final boolean isSameDebtor = debt.getDebtor().equals(originalDebtor);
                    final boolean isSameDebtee = debt.getDebtee().equals(alternativeDebtee);

                    return !isSameDebtor && isSameDebtee;
                })
                .map(Debt::getDebtor)
                .collect(toList());
    }

    private static Optional<AlternativeDebts> findAlternativeDebts(List<Debt> debts, Debt candidate, Person alternativeDebtor, Person alternativeDebtee) {

        // A new debtor covers the debt to the original debtee
        final Optional<Debt> toOriginalDebtee = debts.stream()
                .filter(debt -> debt.getDebtor().equals(alternativeDebtor) && debt.getDebtee().equals(candidate.getDebtee()))
                .findFirst();

        // This one is needed so that the original debtor still pays their debt
        final Optional<Debt> fromOriginalDebtor = debts.stream()
                .filter(debt -> debt.getDebtor().equals(candidate.getDebtor()) && debt.getDebtee().equals(alternativeDebtee))
                .findFirst();

        // The amount of the candidate debt will be subtracted here so that the alternative debtor doesn't pay twice
        final Optional<Debt> compensationDebt = debts.stream()
                .filter(debt -> debt.getDebtor().equals(alternativeDebtor) && debt.getDebtee().equals(alternativeDebtee))
                .findFirst();

        if (toOriginalDebtee.isPresent() && fromOriginalDebtor.isPresent() && compensationDebt.isPresent()) {
            return Optional.of(new AlternativeDebts(toOriginalDebtee.get(), fromOriginalDebtor.get(), compensationDebt.get()));
        } else {
            return Optional.empty();
        }
    }

    private static class AlternativeDebts {
        private final Debt toOriginalDebtee;
        private final Debt fromOriginalDebtor;
        private final Debt compensationDebt;

        private AlternativeDebts(Debt toOriginalDebtee, Debt fromOriginalDebtor, Debt compensationDebt) {
            this.toOriginalDebtee = toOriginalDebtee;
            this.fromOriginalDebtor = fromOriginalDebtor;
            this.compensationDebt = compensationDebt;
        }
    }
}
