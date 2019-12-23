package com.tinhvan.hd.base.util;

/**
 * @author Vlad Mihalcea
 */
public class IntArrayTypeDescriptor
        extends AbstractArrayTypeDescriptor<int[]> {

    private static final long serialVersionUID = -7420533676860411433L;
    public static final IntArrayTypeDescriptor INSTANCE = new IntArrayTypeDescriptor();

    public IntArrayTypeDescriptor() {
        super(int[].class);
    }

    @Override
    public String getSqlArrayType() {
        return "integer";
    }
}
