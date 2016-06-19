package io.nobt.rest.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.nobt.core.domain.Amount;
import io.nobt.core.domain.Expense;
import io.nobt.core.domain.Person;
import io.nobt.rest.json.amount.AmountDeserializer;
import io.nobt.rest.json.amount.AmountSerializer;
import io.nobt.rest.json.expense.ExpenseMixin;
import io.nobt.rest.json.person.PersonDeserializer;
import io.nobt.rest.json.person.PersonSerializer;

/**
 *
 */
public class CoreModule extends SimpleModule {

    public CoreModule() {
        addSerializer(Amount.class, new AmountSerializer());
        addDeserializer(Amount.class, new AmountDeserializer());

        addSerializer(Person.class, new PersonSerializer());
        addDeserializer(Person.class, new PersonDeserializer());

        setMixInAnnotation(Expense.class, ExpenseMixin.class);
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        final ObjectMapper owner = context.getOwner();

        owner.configure(SerializationFeature.INDENT_OUTPUT, true);
        owner.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
