package io.nobt.rest.constraints;

import io.nobt.core.domain.Person;
import io.nobt.core.domain.Share;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class CheckNoDuplicateDebtorsValidator implements ConstraintValidator<CheckNoDuplicateDebtors, List<Share>> {

    @Override
    public void initialize(CheckNoDuplicateDebtors checkNoDuplicateDebtors) {

    }

    @Override
    public boolean isValid(List<Share> shares, ConstraintValidatorContext constraintValidatorContext) {

        final List<Person> allDebtors = shares.stream().map(Share::getDebtor).collect(toList());

        final Set<Person> allUniqueDebtors = new HashSet<>(allDebtors);

        return allDebtors.size() == allUniqueDebtors.size();
    }
}
