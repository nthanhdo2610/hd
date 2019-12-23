package com.tinhvan.hd.sms.bean;

public class OTPLimitRespon {
    private int v_cnt_brief;
    private int v_cnt_24h;

    public OTPLimitRespon(int v_cnt_brief, int v_cnt_24h) {
        this.v_cnt_brief = v_cnt_brief;
        this.v_cnt_24h = v_cnt_24h;
    }

    public int getV_cnt_brief() {
        return v_cnt_brief;
    }

    public void setV_cnt_brief(int v_cnt_brief) {
        this.v_cnt_brief = v_cnt_brief;
    }

    public int getV_cnt_24h() {
        return v_cnt_24h;
    }

    public void setV_cnt_24h(int v_cnt_24h) {
        this.v_cnt_24h = v_cnt_24h;
    }
}
