package com.tinhvan.hd.base.util;

/**
 * @author Vlad Mihalcea
 */
public class VarCharStringArrayTypeDescriptor
        extends StringArrayTypeDescriptor {

    private static final long serialVersionUID = 8056357803571635391L;
    public static final VarCharStringArrayTypeDescriptor INSTANCE =
            new VarCharStringArrayTypeDescriptor();

    @Override
    public String getSqlArrayType() {
        return "varchar";
    }
}
