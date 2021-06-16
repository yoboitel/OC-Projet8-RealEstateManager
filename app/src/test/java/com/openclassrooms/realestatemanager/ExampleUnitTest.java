package com.openclassrooms.realestatemanager;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.Database.Estate;
import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.fragment.FragmentLoan;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void loanCalculateMonthlyPay_isCorrect() {
        Double rightResult = 529.9028930322098;
        assertEquals(rightResult, FragmentLoan.calculateLoanMonthlyPay(100000, 20, 2.5));
    }

    @Test
    public void loanCalculateTotalInterest_isCorrect() {
        Double rightResult = 27176.69432773035;
        assertEquals(rightResult, FragmentLoan.calculateLoanTotalInterest(100000, 20, 2.5));
    }



}