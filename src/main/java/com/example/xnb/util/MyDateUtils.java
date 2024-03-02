package com.example.xnb.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * .
 * 2023/06/07 3:24 下午
 */


public class MyDateUtils {

    public static LocalDateTime dateTimeParse(String time) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDate dateParse(String date) {
        if (date.length() > 10){
            date = date.substring(0,10);
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String dateTimeFormat(LocalDateTime time) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(time);
    }

}
