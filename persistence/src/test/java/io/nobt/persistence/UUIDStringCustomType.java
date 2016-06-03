package io.nobt.persistence;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class UUIDStringCustomType extends AbstractSingleColumnStandardBasicType {

    public UUIDStringCustomType() {
        super(VarcharTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "pg-uuid";
    }
}