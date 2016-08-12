package io.nobt.core.domain.test;

import io.nobt.core.domain.Person;

public final class PersonFactory {

	public static Person thomas = Person.forName("Thomas");
	public static Person matthias = Person.forName("Matthias");
	public static Person simon = Person.forName("Simon");
	public static Person lukas = Person.forName("Lukas");
	public static Person thomasB = Person.forName("Thomas B.");
	public static Person jacqueline = Person.forName("Jacqueline");
	public static Person david = Person.forName("David");
	public static Person harald = Person.forName("Harald");

	private PersonFactory() { }
}
