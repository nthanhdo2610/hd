package com.tinhvan.hd.filehandler.controller;

import com.tinhvan.hd.filehandler.utils.XDocUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {

        File png = new File("A4HoaVanCL2.jpg");
        List<String> lst = Arrays.asList(
                "HD_OFFLINE_ONLINE_ED_Hop_Dong_Tin_Dung_Ap_Dung_Chung_Toan_Bo_ED_FU_TP_SL.docx",
                "HD_PHU_LUC_HOP_DONG_THE_CHAP.docx",
                "HD_ONLINE_CASH_CLFS_Hop_Dong_Tin_Dung.docx",
                "HD_OFFLINE_ONLINE_SVMCECEB_Hop_Dong_Tin_Dung.docx",
                "HD_OFFLINE_ONLINE_CLFS_Hop_Dong_Tin_Dung.docx",
                "HD_Mau_Giay_yeu_cau_bao_hiem.docx",
                "HD_MCSVEC_Hop_dong_the_chap.docx",
                "HD_Dieu_khoan_va_Dieu_kien_Chuong_trinh_hoan_tien.docx",
                "HD_e_Signing_log.docx",
                "HD_THOA_THUAN_DIEU_CHINH_THONG_TIN.docx");
        for (String s : lst) {
            try {
                File docX = new File(s);
                File pdf = XDocUtils.convertPDF("CV" + s.replace(".docx", "") + ".pdf", docX);
                XDocUtils.watermark(s.replace(".docx", "") + ".pdf", pdf, png);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }


}
