package com.noname.util;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateConverter {


    public static String formatToYYYY(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return localDateTime.format(formatter);
    }

    public static String formatMMDD(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        return localDateTime.format(formatter);
    }
    public static String formatToYYYYMMDD(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

    public static String formatToHHMMSS(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static String formatToYYYYMMDDHHMM(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    public static String formatToMMDDAHHMM(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd a hh:mm");
        return localDateTime.format(formatter);
    }

    
    
    public static long timeDifferenceToMillis(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(currentDateTime, dateTime);
        return Math.abs(duration.toMillis()); 
    }


    public static String formatTimeAgo(LocalDateTime pastTime) {

        Duration duration = Duration.between(pastTime, LocalDateTime.now());

        if (duration.toMinutes() < 1) {
            long seconds = duration.getSeconds();
            return seconds + "초전";
        } else if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + "분전";
        } else if (duration.toHours() < 24) {
            long hours = duration.toHours();
            return hours + "시간전";
        } else {
            return formatToYYYYMMDD(pastTime);
        }
    }

    public static String formatTimeAgoHourOrDate(LocalDateTime pastTime) {
        Duration duration = Duration.between(pastTime, LocalDateTime.now());
        if (duration.toHours() < 24) {
            return formatToHHMMSS(pastTime);
        } else {
            return formatToYYYYMMDD(pastTime);
        }
    }










        }
