package io.nobt.rest.json.nobt;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.nobt.core.domain.NobtId;

import java.io.IOException;

public class NobtIdSerializer extends JsonSerializer<NobtId> {

    @Override
    public void serialize(NobtId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}
