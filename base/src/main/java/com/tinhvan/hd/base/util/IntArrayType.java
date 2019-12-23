package com.tinhvan.hd.base.util;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * @author Vlad Mihalcea
 */
public class IntArrayType
        extends AbstractSingleColumnStandardBasicType<int[]>
        implements DynamicParameterizedType {

    private static final long serialVersionUID = 5541788716017937801L;
    public static final IntArrayType INSTANCE = new IntArrayType();

    public IntArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, IntArrayTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "int-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((IntArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
