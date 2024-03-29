package icu.develop.expression.filter.utils;


import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Number utils
 *
 * @author Jiaju Zhuang
 */
public class NumberUtils {

    private NumberUtils() {
    }

    @Data
    public static class FormatProperty {
        public static final FormatProperty EMPTY = new FormatProperty();
        private String format;
        private RoundingMode roundingMode;
    }

    private static boolean hasFormat(FormatProperty formatProperty) {
        return formatProperty != null
                && !StringUtils.isEmpty(formatProperty.getFormat());
    }


    public static String format(Number num, FormatProperty formatProperty) {
        if (formatProperty == null
                || StringUtils.isEmpty(formatProperty.getFormat())) {
            if (num instanceof BigDecimal) {
                return ((BigDecimal) num).toPlainString();
            } else {
                return num.toString();
            }
        }
        String format = formatProperty.getFormat();
        RoundingMode roundingMode = formatProperty.getRoundingMode();
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(roundingMode);
        return decimalFormat.format(num);
    }

    public static Short parseShort(String string, FormatProperty formatProperty) throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string).shortValue();
        }
        return parse(string, formatProperty).shortValue();
    }

    public static Long parseLong(String string, FormatProperty formatProperty) throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string).longValue();
        }
        return parse(string, formatProperty).longValue();
    }

    /**
     * parse Integer from string
     *
     * @param string         An integer read in string format
     * @param formatProperty Properties of the content read in
     * @return An integer converted from a string
     * @throws ParseException exception
     */
    public static Integer parseInteger(String string, FormatProperty formatProperty) throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string).intValue();
        }
        return parse(string, formatProperty).intValue();
    }


    public static Float parseFloat(String string, FormatProperty formatProperty) throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string).floatValue();
        }
        return parse(string, formatProperty).floatValue();
    }


    public static BigDecimal parseBigDecimal(String string, FormatProperty formatProperty)
            throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string);
        }
        return new BigDecimal(parse(string, formatProperty).toString());
    }

    public static Byte parseByte(String string, FormatProperty formatProperty) throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string).byteValue();
        }
        return parse(string, formatProperty).byteValue();
    }


    public static Double parseDouble(String string, FormatProperty formatProperty) throws ParseException {
        if (!hasFormat(formatProperty)) {
            return new BigDecimal(string).doubleValue();
        }
        return parse(string, formatProperty).doubleValue();
    }


    private static Number parse(String string, FormatProperty formatProperty) throws ParseException {
        String format = formatProperty.getFormat();
        RoundingMode roundingMode = formatProperty.getRoundingMode();
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(roundingMode);
        decimalFormat.setParseBigDecimal(true);
        return decimalFormat.parse(string);
    }

}
