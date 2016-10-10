package io.nobt.persistence;

import com.fasterxml.jackson.databind.JavaType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.usertype.DynamicParameterizedType;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * @author Vlad Mihalcea
 */
public class JsonTypeDescriptor extends AbstractTypeDescriptor<Object> implements DynamicParameterizedType {

    private JavaType fieldType;

    @Override
    public void setParameterValues(Properties parameters) {

        final Class entityType = resolveEntityClass(parameters);
        final String property = (String) parameters.get(DynamicParameterizedType.PROPERTY);

        final Type genericType = getActualType(entityType, property);

        fieldType = JacksonUtil.OBJECT_MAPPER.constructType(genericType);
    }

    private Class<?> resolveEntityClass(Properties parameters) {
        try {
            return Class.forName((String) parameters.get(DynamicParameterizedType.ENTITY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Type getActualType(Class entityType, String property) {
        try {
            return entityType.getDeclaredField(property).getGenericType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonTypeDescriptor() {
        super( Object.class, new MutableMutabilityPlan<Object>() {
            @Override
            protected Object deepCopyNotNull(Object value) {
                return SerializationUtils.clone((Serializable) value);
            }
        });
    }

    @Override
    public boolean areEqual(Object one, Object another) {
        if ( one == another ) {
            return true;
        }
        if ( one == null || another == null ) {
            return false;
        }

        return one.equals(another);
    }

    @Override
    public String toString(Object value) {
        return JacksonUtil.toString(value);
    }

    @Override
    public Object fromString(String string) {
        return JacksonUtil.fromString(string, fieldType);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public <X> X unwrap(Object value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( String.class.isAssignableFrom( type ) ) {
            return (X) toString(value);
        }
        if ( Object.class.isAssignableFrom( type ) ) {
            return (X) JacksonUtil.toJsonNode(toString(value));
        }
        throw unknownUnwrap( type );
    }

    @Override
    public <X> Object wrap(X value, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        return fromString(value.toString());
    }

}
