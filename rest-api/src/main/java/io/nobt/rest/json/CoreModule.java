package io.nobt.rest.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.nobt.core.domain.*;
import io.nobt.rest.json.amount.AmountDeserializer;
import io.nobt.rest.json.amount.AmountSerializer;
import io.nobt.rest.json.expense.ExpenseMixin;
import io.nobt.rest.json.nobt.NobtIdSerializer;
import io.nobt.rest.json.nobt.NobtMixin;
import io.nobt.rest.json.person.PersonDeserializer;
import io.nobt.rest.json.person.PersonSerializer;
import io.nobt.rest.json.share.ShareMixin;

public class CoreModule extends SimpleModule {

    public CoreModule() {
        addSerializer(Amount.class, new AmountSerializer());
        addDeserializer(Amount.class, new AmountDeserializer());

        addSerializer(Person.class, new PersonSerializer());
        addDeserializer(Person.class, new PersonDeserializer());

        addSerializer(NobtId.class, new NobtIdSerializer());

        setMixInAnnotation(Share.class, ShareMixin.class);
        setMixInAnnotation(Nobt.class, NobtMixin.class);
        setMixInAnnotation(Expense.class, ExpenseMixin.class);
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);

        final ObjectMapper owner = context.getOwner();

        owner.configure(SerializationFeature.INDENT_OUTPUT, true);
        owner.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        owner.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
