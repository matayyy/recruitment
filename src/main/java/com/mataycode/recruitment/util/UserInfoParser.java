package com.mataycode.recruitment.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UserInfoParser {

    public static String extractGender(Map<String, Object> userInfo) {
        List<Map<String, Object>> genders = (List<Map<String, Object>>) userInfo.get("genders");
        if (genders != null && !genders.isEmpty()) {
            Object value = genders.get(0).get("value");
            if (value instanceof String) {
                return ((String) value).toUpperCase();
            }
        }
        return "OTHER";
    }

    public static LocalDate extractBirthdate(Map<String, Object> userInfo) {
        List<Map<String,Object>> birthdays = (List<Map<String, Object>>) userInfo.get("birthdays");
        Map<String, Object> date = (Map<String, Object>) birthdays.get(0).get("date");
        if (birthdays != null && !birthdays.isEmpty()) {
            LocalDate birthdate = LocalDate.of(
                    (int) date.get("year"),
                    (int) date.get("month"),
                    (int) date.get("day")
            );
            return birthdate;
        }
        return LocalDate.of(2000, 1, 1);
    }
}
