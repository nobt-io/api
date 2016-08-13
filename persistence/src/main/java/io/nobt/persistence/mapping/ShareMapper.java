package io.nobt.persistence.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import de.undercouch.bson4jackson.BsonFactory;
import io.nobt.core.domain.Share;
import io.nobt.persistence.mapping.bson.BsonDomainModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Set;

public class ShareMapper {

    private ObjectMapper bsonMapper;
    private CollectionLikeType shareDtoListType;

    public ShareMapper() {
        bsonMapper = new ObjectMapper(new BsonFactory());
        bsonMapper.registerModule(new BsonDomainModule());

        shareDtoListType = bsonMapper.getTypeFactory().constructCollectionLikeType(Set.class, Share.class);
    }

    public Set<Share> mapToShareSet(byte[] sharesAsBytes) {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sharesAsBytes);

        return readIntoBeans(inputStream);
    }

    public byte[] mapToByteArray(List<Share> shares) {

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeBeans(shares, outputStream);

        return outputStream.toByteArray();
    }

    private Set<Share> readIntoBeans(ByteArrayInputStream bais) {
        try {
            return bsonMapper.readValue(bais, shareDtoListType);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeBeans(List<Share> shares, ByteArrayOutputStream boas) {
        try {
            bsonMapper.writeValue(boas, shares);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
