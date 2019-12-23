package com.tinhvan.hd.base;

public final class HDConstant {


    public static final class STATUS {
        public static final int DISABLE = 0;
        public static final int ENABLE = 1;
        public static final int DELETE_FOREVER = 2;
    }

    public static final class ROLE {
        public static final int CUSTOMER = 2;
        //public static final int ADMINISTRATOR = 1000;
        public static final int STAFF = 1001;
    }

    public static final class CUSTOMER_IMAGE {
        public static final int AVATAR = 1;
        public static final int CMND_FRONT = 2;
        public static final int CMND_BACKSIDE = 3;
        public static final int PASSPORT = 4;
    }

    public static final class ENVIRONMENT {
        public static final String APP = "APP";
        public static final String WEB_ESIGN = "WEB-ESIGN";
        public static final String WEB_ADMIN = "WEB-ADMIN";
    }

    public static final String SYSTEM_CONFIG = "SYSTEM_CONFIG";

    public static final String ROLES = "ROLES";
}
