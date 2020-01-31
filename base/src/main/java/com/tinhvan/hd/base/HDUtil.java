package com.tinhvan.hd.base;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.base.enities.StaffLogAction;


public final class HDUtil {

    /**
     * The Constant REPLACE_CHARS.
     */
    private static final String REPLACE_CHARS = "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêế�?ểễệiíìỉĩịuúùủũụưứừửữựoóò�?õ�?ôốồổỗộơớ�?ởỡợyýỳỷỹỵ";


    private static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static long getUnixTimeNow() {
        return Instant.now().getEpochSecond();
    }

    public static long getUnixTime(Date time) {
        return time.getTime() / 1000L;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static boolean isMatch(String pattern, String value) {
        Pattern r = Pattern.compile(pattern);
        Matcher matches = r.matcher(value);
        if (matches.find()) {
            return true;
        }
        return false;
    }

    public static boolean isPhoneNumber(String value) {
        if (isNullOrEmpty(value)) return false;
        String pattern = new StringBuilder()
                .append("(^0(86|96|97|98|32|33|34|35|36|37|38|39|89|90|93|70|79")
                .append("|77|76|78|88|91|94|83|84|85|81|82|92|56|58|99|59|52)")
                .append("+\\d{7}$)").toString();
        return isMatch(pattern, value);
    }

    public static boolean isEmail(String value) {
        if (isNullOrEmpty(value)) return false;
        String pattern = new StringBuilder()
                .append("^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|")
                .append("[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])")
                .append("+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|")
                .append("[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|")
                .append("((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?")
                .append("(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|")
                .append("[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900")
                .append("-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c")
                .append("\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-")
                .append("\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)")
                .append("?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF")
                .append("\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-")
                .append("\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-")
                .append("\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|")
                .append("[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+")
                .append("(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])")
                .append("|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])")
                .append("([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF")
                .append("\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\")
                .append("uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$").toString();
        return isMatch(pattern, value.toLowerCase());
    }

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c)
            r.add(clazz.cast(o));
        return r;
    }


    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'Ỳ', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'ỳ',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }


    public static String unAccent(String str) {

        if (!HDUtil.isNullOrEmpty(str)) {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

            str = pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "");

            str = str.replaceAll("[^A-Za-z0-9]", "");
        }
        return str;

    }


    /**
     * Applies the specified mask to the card number.
     *
     * @param number The card number in plain format
     * @param mask   The number mask pattern. Use # to include a digit from the
     *               card number at that position, use x to skip the digit at that position
     * @return The masked card number
     */
    public static String maskNumber(String number, String mask) {

        // format the number
        number = number.replaceAll("\\s", "");
        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(number.charAt(index));
                index++;
            } else if (c == '*') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        // return the masked number
        return maskedNumber.toString();
    }

    public static String formatEmailSave(String email) {
        if (!HDUtil.isNullOrEmpty(email)) {
            String[] parts = email.split("@");
            String firstEmail = parts[0];
            String fm = firstEmail.substring(0, firstEmail.length() / 2);
            int len = fm.length();
            StringBuilder a = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                a.append('*');
            }
            email = firstEmail.replace(fm, a) + "@" + parts[1];
        }
        return email;
    }

    public static Object writeLogAction(RequestDTO req, String name, String action, String para, String oldValues, String newValues, String type) {
        if (req.jwt() != null) {
            try {
                if (req.jwt().getRole().equals(HDConstant.ROLE.CUSTOMER)) {
                    CustomerLogAction customerLogAction = new CustomerLogAction();
                    customerLogAction.setObjectName(name);
                    customerLogAction.setAction(action);
                    customerLogAction.setCreatedAt(req.now());
                    customerLogAction.setCreatedBy(req.jwt().getUuid());
                    customerLogAction.setPara(para);
                    customerLogAction.setDevice(req.environment());
                    customerLogAction.setValueOld(oldValues);
                    customerLogAction.setValueNew(newValues);
                    customerLogAction.setType(type);
                    return customerLogAction;
                } else {
                    StaffLogAction staffLogAction = new StaffLogAction();
                    staffLogAction.setObjectName(name);
                    staffLogAction.setAction(action);
                    staffLogAction.setCreatedAt(req.now());
                    staffLogAction.setCreatedBy(req.jwt().getUuid());
                    staffLogAction.setPara(para);
                    staffLogAction.setStaffId(req.jwt().getUuid());
                    staffLogAction.setDevice(req.environment());
                    staffLogAction.setValueOld(oldValues);
                    staffLogAction.setValueNew(newValues);
                    staffLogAction.setType(type);
                    return staffLogAction;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return null;
    }

    public static Date setBeginDay(Date date) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atTime(00, 00, 00, 000);
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }
    public static Date setEndDay(Date date) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atTime(23, 59, 59, 999);
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

//    public static void main(String[] args) {
//        System.out.println(maskNumber("*** *** 9781","*** *** ####"));
//    }
}
