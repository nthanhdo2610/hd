package com.tinhvan.hd.base.util;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * @author Vlad MIhalcea
 */
@SuppressWarnings("rawtypes")
public class EnumArrayType
        extends AbstractSingleColumnStandardBasicType<Enum[]>
        implements DynamicParameterizedType {

    private static final long serialVersionUID = 459313099230451387L;

    public EnumArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, EnumArrayTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "enum-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((EnumArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
