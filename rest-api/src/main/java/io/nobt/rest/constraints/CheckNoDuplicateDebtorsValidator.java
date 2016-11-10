package io.nobt.rest.constraints;

import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CheckNoDuplicateDebtorsValidator implements ConstraintValidator<CheckNoDuplicateDebtors, List<Share>> {

    @Override
    public void initialize(CheckNoDuplicateDebtors checkNoDuplicateDebtors) {

    }

    @Override
    public boolean isValid(List<Share> shares, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (shares == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("must not be null");
            return false;
        }

        final List<Person> allDebtors = shares.stream().map(Share::getDebtor).collect(toList());

        final HashSet<Person> visitedDebtors = new HashSet<>();

        for (int i = 0; i < allDebtors.size(); i++) {
            Person debtor = allDebtors.get(i);
            if (visitedDebtors.contains(debtor)) {
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Debtors must only occur once across all shares.")
                        .addPropertyNode("debtor")
                            .inIterable().atIndex(i)
                        .addConstraintViolation();
            }

            visitedDebtors.add(debtor);
        }

        return visitedDebtors.size() == allDebtors.size();
    }
}
