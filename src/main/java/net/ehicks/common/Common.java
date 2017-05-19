package net.ehicks.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

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
}
