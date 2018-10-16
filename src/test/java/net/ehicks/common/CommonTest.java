package net.ehicks.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CommonTest
{
    @Test
    public void toMetric()
    {
        Map<Long, String> inputToExpectedResult = new HashMap<>();
        inputToExpectedResult.put(123L, "123 B");
        inputToExpectedResult.put(1234L, "1 KiB");
        inputToExpectedResult.put(12345L, "12 KiB");
        inputToExpectedResult.put(123456L, "121 KiB");
        inputToExpectedResult.put(1234567L, "1 MiB");
        inputToExpectedResult.put(12345678L, "12 MiB");
        inputToExpectedResult.put(123456789L, "118 MiB");
        inputToExpectedResult.put(1234567890L, "1 GiB");

        testExpectedValues(inputToExpectedResult, "B");

        inputToExpectedResult.clear();
        inputToExpectedResult.put(123L, "123 m");
        inputToExpectedResult.put(1234L, "1 km");
        inputToExpectedResult.put(12345L, "12 km");
        inputToExpectedResult.put(123456L, "123 km");
        inputToExpectedResult.put(1234567L, "1 Mm");
        inputToExpectedResult.put(12345678L, "12 Mm");
        inputToExpectedResult.put(123456789L, "123 Mm");
        inputToExpectedResult.put(1234567890L, "1 Gm");

        testExpectedValues(inputToExpectedResult, "m");
    }

    private void testExpectedValues(Map<Long, String> inputToExpectedResult, String unit)
    {
        for (Long input : inputToExpectedResult.keySet())
        {
            String result = Common.toMetric(input, unit);
            String expected = inputToExpectedResult.get(input);

            Assertions.assertEquals(result, expected, "Common.toMetric(" + input + ") returns " + result + " instead of " + expected);
        }
    }
}
