package com.tinhvan.hd.base.util;

/**
 * @author Vlad Mihalcea
 */
public class StringArrayTypeDescriptor
        extends AbstractArrayTypeDescriptor<String[]> {

    private static final long serialVersionUID = -8799638611549145624L;
    public static final StringArrayTypeDescriptor INSTANCE = new StringArrayTypeDescriptor();

    public StringArrayTypeDescriptor() {
        super(String[].class);
    }

    @Override
    public String getSqlArrayType() {
        return "text";
    }
}
