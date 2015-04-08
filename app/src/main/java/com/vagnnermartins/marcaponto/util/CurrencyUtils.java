package com.vagnnermartins.marcaponto.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CurrencyUtils {

    public static String format(BigDecimal value){
        return NumberFormat.getCurrencyInstance().format(value);
    }
}