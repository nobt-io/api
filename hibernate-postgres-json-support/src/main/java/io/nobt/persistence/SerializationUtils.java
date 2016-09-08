package io.nobt.persistence;

import java.io.*;

public class SerializationUtils {

    public static Object clone(Serializable object) {
        return deserialize(serialize(object));
    }

    public static void serialize(Serializable obj, OutputStream outputStream) {
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(obj);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) {
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            return in.readObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object deserialize(byte[] objectData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        return deserialize(bais);
    }
}