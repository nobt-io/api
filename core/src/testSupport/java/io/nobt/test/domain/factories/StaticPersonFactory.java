package io.nobt.test.domain.factories;

import io.nobt.core.domain.Person;

public final class StaticPersonFactory {

	public static Person thomas = Person.forName("Thomas");
	public static Person matthias = Person.forName("Matthias");
	public static Person simon = Person.forName("Simon");
	public static Person eva = Person.forName("Eva");
	public static Person lukas = Person.forName("Lukas");
	public static Person thomasB = Person.forName("Thomas B.");
	public static Person jacqueline = Person.forName("Jacqueline");
	public static Person david = Person.forName("David");
	public static Person harald = Person.forName("Harald");
	public static Person martin = Person.forName("Martin");

	private StaticPersonFactory() { }
}
