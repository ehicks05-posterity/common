package net.ehicks.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Common
{
    private static final Logger log = LoggerFactory.getLogger(Common.class);

    public static void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public static String toMetric(long in)
    {
        double value = in;
        List<String> prefixes = Arrays.asList("", "k", "M", "G");
        int unit = 0;
        while (value > 1024)
        {
            value /= 1024;
            unit++;
        }
        return String.format("%.0f", value) + " " + prefixes.get(unit) + "B";
    }

    public static String limit(String input, int limit)
    {
        if (input != null && input.length() > limit)
        {
            return input.substring(0, limit) + "...";
        }
        return input;
    }

    public static String capFirstLetter(String input)
    {
        return input.substring(0, 1).toUpperCase() + input.substring(1, input.length());
    }

    public static String getSafeString(String input)
    {
        if (input == null) return "";
        return input;
    }

    public static String[] getSafeStringArray(String[] input)
    {
        if (input == null) return new String[0];
        return input;
    }

    //
    // TYPE CONVERSIONS
    //

    public static String arrayToString(String[] input)
    {
        if (input == null)
            return "";
        String output = "";
        for (String inputString : input)
        {
            if (output.length() > 0)
                output += ",";
            output += inputString;
        }
        return output;
    }

    public static String byteArrayToBase64(byte[] input)
    {
        return Base64.getEncoder().encodeToString(input);
    }

    public static java.sql.Date utilDateToSQLDateTime(Date utilDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        return new java.sql.Date(cal.getTime().getTime()); // your sql date
    }

    public static java.sql.Date utilDateToSQLDate(Date utilDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(utilDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTime().getTime()); // your sql date
    }

    public static Date stringToDate(String input)
    {
        Date result = null;
        if (input == null || input.length() == 0) return null;
        try
        {
            result = new SimpleDateFormat("yyyy-MM-dd").parse(input);
        }
        catch (ParseException e)
        {
            log.error(e.getMessage(), e);
        }
        if (result == null)
        {
            try
            {
                result = new SimpleDateFormat("MM/dd/yyyy").parse(input);
            }
            catch (ParseException e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static int stringToInt(String input)
    {
        int result = 0;
        if (input == null || input.length() == 0) return result;
        input = input.replaceAll(",","");
        try
        {
            result = Integer.parseInt(input);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public static long stringToLong(String input)
    {
        long result = 0;
        if (input == null || input.length() == 0) return result;
        input = input.replaceAll(",","");
        try
        {
            result = Long.parseLong(input);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public static BigDecimal stringToBigDecimal(String input)
    {
        BigDecimal result = BigDecimal.ZERO;
        if (input == null || input.length() == 0) return result;
        try
        {
            result = new BigDecimal(input);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public static BigDecimal integerToBigDecimal(Integer input)
    {
        BigDecimal result = BigDecimal.ZERO;
        if (input == null) return result;
        try
        {
            result = new BigDecimal(input);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
