package com.tinhvan.hd.base.util;

/**
 * @author Vlad Mihalcea
 */
public class VarCharStringArrayType
        extends StringArrayType {

    private static final long serialVersionUID = 9191157149450168141L;
    public static final VarCharStringArrayType INSTANCE = new VarCharStringArrayType();

    public VarCharStringArrayType() {
        super(VarCharStringArrayTypeDescriptor.INSTANCE);
    }
}
