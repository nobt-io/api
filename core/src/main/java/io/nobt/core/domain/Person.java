package io.nobt.core.domain;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class Person {

	@Pattern(regexp = "[^;]+")
	private String name;

	public static Person forName(String name) {
		return new Person(name);
	}

	public Person(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Person)) return false;
		Person person = (Person) o;
		return Objects.equals(name, person.name);
	}

	@Override
	public String toString() {
		return name;
	}
}
