package com.tinhvan.hd.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date covertStringToDate(String strDate){
        if (strDate != null) {
            String checkFormat = strDate.substring(0,10);
            String[] check = checkFormat.split("-");
            SimpleDateFormat formatter = null;
            if (check[0].length() == 2) {
                formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }

            Date date = null;
            try {

                date = formatter.parse(strDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
       return null;
    }

    public static Date covertStringToDateFromMiddle(String strDate){
        if (strDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {

                date = formatter.parse(strDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    public static String convertDateToString(Date date) {
        if(date != null){
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = dateFormat.format(date);
            return strDate;
        }
        return "";
    }

}
