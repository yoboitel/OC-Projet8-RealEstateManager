package com.openclassrooms.realestatemanager;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void dollarsToEurosConversion_isCorrect() {
        assertEquals(81, Utils.convertDollarToEuro(100));
    }

    @Test
    public void EurosToDollarsConversion_isCorrect() {
        assertEquals(100, Utils.convertEuroToDollar(81));
    }

    @Test
    public void todayDateFormat_isCorrect() {
        String correctDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        assertEquals(correctDate, Utils.getTodayDate());
    }
}