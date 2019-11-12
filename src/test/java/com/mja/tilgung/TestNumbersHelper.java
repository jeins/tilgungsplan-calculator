package com.mja.tilgung;

import com.mja.tilgung.utils.NumbersHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNumbersHelper {

    @Test
    public void formatDENumber_WithNum_ShouldReturnFormattedNum() {
        double num = 100000;
        String expectedValue = "100.000,00";
        String result = NumbersHelper.formatDENumber(num);

        Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void formatDENumber_WithDecimalNum_ShouldReturnFormattedNum() {
        double num = 12345.67;
        String expectedValue = "12.345,67";

        String result = NumbersHelper.formatDENumber(num);

        Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void reformatDENumber_withFormattedNumShouldReturnDecimal() {
        String num = "12.345,67";
        double expectedValue = 12345.67;

        double result = NumbersHelper.reformatDENumber(num);

        Assert.assertEquals(expectedValue, result, 2);
    }

    @Test
    public void roundWithMoreThanTwoDigitShouldReturnTwoDigitAfterComma() {
        double num = 123.3333;
        double expectedValue = 123.33;
        double result = NumbersHelper.round(num);

        Assert.assertEquals(Double.toString(expectedValue), Double.toString(result));
    }

    @Test
    public void roundWithMoreThanTwoDigitShouldReturnTwoDigitAfterCommaAndRoundUp() {
        double num = 123.56777;
        double expectedValue = 123.57;
        double result = NumbersHelper.round(num);

        Assert.assertEquals(Double.toString(expectedValue), Double.toString(result));
    }
}
