package net.ehicks.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CommonTest
{
    @Test
    public void toMetric()
    {
        Map<Long, String> inputToExpectedResult = new HashMap<>();
        inputToExpectedResult.put(123L, "123 B");
        inputToExpectedResult.put(1234L, "1 kB");
        inputToExpectedResult.put(12345L, "12 kB");
        inputToExpectedResult.put(123456L, "121 kB");
        inputToExpectedResult.put(1234567L, "1 MB");
        inputToExpectedResult.put(12345678L, "12 MB");
        inputToExpectedResult.put(123456789L, "118 MB");

        for (Long input : inputToExpectedResult.keySet())
        {
            String result = Common.toMetric(input);
            String expected = inputToExpectedResult.get(input);

            Assert.assertTrue("Common.toMetric(" + input + ") returns " + result + " instead of " + expected, result.equals(expected));
        }

    }
}
