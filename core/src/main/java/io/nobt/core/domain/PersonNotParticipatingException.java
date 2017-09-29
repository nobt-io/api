package io.nobt.core.domain;

public class PersonNotParticipatingException extends RuntimeException {

    private final Person person;

    public PersonNotParticipatingException(Person person) {
        this.person = person;
    }

    @Override
    public String getMessage() {
        return String.format("Person '%s' does not participate in the nobt.", this.person);
    }
}
