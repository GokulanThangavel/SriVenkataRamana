package com.example.googlesheets.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SafeParser {

    public static Integer safeParseInt(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long safeParseLong(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double safeParseDouble(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Date safeParseDate(String value, String format) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(value.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    // Optional default-date version
    public static Date safeParseDateOrDefault(String value, String format, Date defaultDate) {
        Date parsed = safeParseDate(value, format);
        return parsed != null ? parsed : defaultDate;
    }
}
