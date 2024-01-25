package com.github.akafasty.economy.util;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class NumberFormatter {

    private final String[] symbols = {"", "K", "M", "B", "T", "Q"};
    private final BigDecimal divisor = new BigDecimal("1000");
    private final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));

    public String numberFormat(double value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));
        numberFormat.setMaximumFractionDigits(1);
        return numberFormat.format(value);
    }

    public static String numberToString(BigDecimal value) {

        int index = 0;

        BigDecimal aux;
        BigDecimal tmp;

        for(aux = value; (tmp = aux.divide(divisor)).compareTo(BigDecimal.ONE) >= 0 && index < symbols.length - 1; ++index) {
            aux = tmp;
        }

        return numberFormat(aux.doubleValue()) + symbols[index];
    }

    public static BigDecimal stringToNumber(String value) {

        value = value.toUpperCase();

        try {

            Matcher matcher = PATTERN.matcher(value);

            if (!matcher.find())
                return decimalParser(value);

            BigDecimal amount = new BigDecimal(matcher.group(1));
            String suffix = matcher.group(2);

            int index = List.of(symbols)
                    .indexOf(suffix);

            return amount.multiply(divisor.pow(index));

        }

        catch (Exception exception) { return decimalParser(value); }
    }

    /**
     * @param string string to be parsed
     * @return string parsed to integer
     */
    public static Integer integerParser(String string) {

        try { return Integer.parseInt(string); }
        catch (Exception exception) { return null; }

    }

    public static BigDecimal decimalParser(String string) {

        try { return new BigDecimal(string); }
        catch (Exception exception) { return new BigDecimal("0.0"); }

    }

    public String simpleFormat(long time) {

        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        final String date = dateFormat.format(new Date(time));

        final String day = date.split(" ")[0];
        final String hour = date.split(" ")[1];

        return String.format("%s às %s", day, hour);

    }

}
