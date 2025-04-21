package org.ignacioScript.co.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumericUtils {

    public static double roundDouble(double input, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places must be non-negative");
        }
        BigDecimal bd = BigDecimal.valueOf(input);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
