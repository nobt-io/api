package io.nobt.rest.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.nobt.core.domain.*;
import io.nobt.rest.json.amount.AmountDeserializer;
import io.nobt.rest.json.amount.AmountSerializer;
import io.nobt.rest.json.currency.CurrencyKeyDeserializer;
import io.nobt.rest.json.currency.CurrencyKeySerializer;
import io.nobt.rest.json.expense.ConversionInformationMixin;
import io.nobt.rest.json.expense.ExpenseMixin;
import io.nobt.rest.json.nobt.NobtIdSerializer;
import io.nobt.rest.json.nobt.NobtMixin;
import io.nobt.rest.json.person.PersonDeserializer;
import io.nobt.rest.json.person.PersonSerializer;
import io.nobt.rest.json.share.ShareMixin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoreModule extends SimpleModule {

    public CoreModule() {
        addSerializer(Amount.class, new AmountSerializer());
        addDeserializer(Amount.class, new AmountDeserializer());

        addSerializer(Person.class, new PersonSerializer());
        addDeserializer(Person.class, new PersonDeserializer());

        addSerializer(NobtId.class, new NobtIdSerializer());

        addSerializer(CurrencyKey.class, new CurrencyKeySerializer());
        addDeserializer(CurrencyKey.class, new CurrencyKeyDeserializer());

        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        setMixInAnnotation(Share.class, ShareMixin.class);
        setMixInAnnotation(Nobt.class, NobtMixin.class);
        setMixInAnnotation(Expense.class, ExpenseMixin.class);
        setMixInAnnotation(ConversionInformation.class, ConversionInformationMixin.class);
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
