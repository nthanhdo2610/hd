package com.tinhvan.hd.base.util;

import java.util.Properties;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;

/**
 * @author Vlad MIhalcea
 */
public class StringArrayType
        extends AbstractSingleColumnStandardBasicType<String[]>
        implements DynamicParameterizedType {

    private static final long serialVersionUID = -1092638625503030054L;

    public StringArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, StringArrayTypeDescriptor.INSTANCE);
    }

    public StringArrayType(JavaTypeDescriptor<String[]> javaTypeDescriptor) {
        super(ArraySqlTypeDescriptor.INSTANCE, javaTypeDescriptor);
    }

    public String getName() {
        return "string-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((StringArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
