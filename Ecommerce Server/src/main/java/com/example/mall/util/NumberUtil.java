package com.example.mall.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NumberUtil {

    private static final Pattern PHONE_NUMBER_PATTERN =Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-8])|(18[0-9]))\\d{8}$");

    public boolean isPhoneNumber(String string){
        return PHONE_NUMBER_PATTERN.matcher(string).matches();
    }

    public boolean isNotPhoneNumber(String string){
        return !isPhoneNumber(string);
    }

}
