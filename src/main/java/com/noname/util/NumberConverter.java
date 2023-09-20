package com.noname.util;

import java.text.DecimalFormat;

public class NumberConverter {

    public static String formatNumberWithCommas(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }
}
