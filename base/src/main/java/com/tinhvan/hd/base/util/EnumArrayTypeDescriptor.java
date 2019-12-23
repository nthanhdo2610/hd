package com.tinhvan.hd.base.util;

/**
 * @author Vlad Mihalcea
 */
@SuppressWarnings("rawtypes")
public class EnumArrayTypeDescriptor
        extends AbstractArrayTypeDescriptor<Enum[]> {

    private static final long serialVersionUID = 8390989121522396661L;
    public static final EnumArrayTypeDescriptor INSTANCE = new EnumArrayTypeDescriptor();

    public EnumArrayTypeDescriptor() {
        super(Enum[].class);
    }

    @Override
    public String getSqlArrayType() {
        return "integer";
    }
}
