package io.nobt.test.domain.factories;

import io.nobt.core.domain.Person;

import java.util.List;
import java.util.Random;

public class RandomPersonFactory {

    public static Random randomPersonIndexGenerator = new Random();

    public static Person randomPerson() {

        final List<Person> allPersons = StaticPersonFactory.ALL;
        final int randomIndex = randomPersonIndexGenerator.nextInt(100000);

        return allPersons.get(randomIndex % allPersons.size());
    }

}
