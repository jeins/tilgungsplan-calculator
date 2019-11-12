package com.mja.tilgung;

import com.mja.tilgung.models.RepaymentPlan;
import com.mja.tilgung.models.RepaymentRequest;
import com.mja.tilgung.services.RepaymentPlanServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
public class TestRepaymentPlanServices {
    private RepaymentRequest repaymentRequest;
    private RepaymentPlanServices repaymentPlanServices;
    private SimpleDateFormat simpleDateFormat;
    private RepaymentPlan previousRepaymentPlan;

    @Before
    public void setup() throws ParseException {
        String dateFormat = "dd.MM.yyyy";

        simpleDateFormat = new SimpleDateFormat(dateFormat);
        repaymentRequest = new RepaymentRequest();
        repaymentPlanServices = new RepaymentPlanServices();

        mocksRepaymentRequest();

        repaymentPlanServices.setRepaymentRequest(repaymentRequest);

        previousRepaymentPlan = repaymentPlanServices.getFirstMonthRepaymentPlan();
    }

    private void mocksRepaymentRequest() throws ParseException {
        repaymentRequest.setLoan(100000);
        repaymentRequest.setInitialRepayment(2);
        repaymentRequest.setStartDate("01.11.2015");
        repaymentRequest.setInterestRate(2.12);
        repaymentRequest.setDurationFixedInterest(10);
    }

    @Test
    public void generateRepaymentPlanCheckSecondMonthPlanShouldValue() throws ParseException {
        double expectedRate = 343.33;
        double expectedInterest = 176.67;
        double expectedRepayment = 166.66;
        double expectedRemainingDebt = -99833.34;
        int monthIndex = 1;
        String expectedDate = "31.12.2015";

        List<RepaymentPlan> repaymentPlans = repaymentPlanServices.generateRepaymentPlan();

        RepaymentPlan expectedResult = new RepaymentPlan();
        expectedResult.setDate(simpleDateFormat.parse(expectedDate));
        expectedResult.setRemainingDebt(expectedRemainingDebt);
        expectedResult.setRate(expectedRate);
        expectedResult.setInterest(expectedInterest);
        expectedResult.setRepayment(expectedRepayment);

        Assert.assertEquals(expectedResult, repaymentPlans.get(monthIndex));
    }

    @Test
    public void generateRepaymentPlanCheckLastMonthPlanShouldValue() throws ParseException {
        double expectedRate = 343.33;
        double expectedInterest = 137.71;
        double expectedRepayment = 205.62;
        double expectedRemainingDebt = -77744.14;
        int monthIndex = 120;
        String expectedDate = "30.11.2025";

        List<RepaymentPlan> repaymentPlans = repaymentPlanServices.generateRepaymentPlan();

        RepaymentPlan expectedResult = new RepaymentPlan();
        expectedResult.setDate(simpleDateFormat.parse(expectedDate));
        expectedResult.setRemainingDebt(expectedRemainingDebt);
        expectedResult.setRate(expectedRate);
        expectedResult.setInterest(expectedInterest);
        expectedResult.setRepayment(expectedRepayment);

        Assert.assertEquals(expectedResult, repaymentPlans.get(monthIndex));
    }

    @Test
    public void getRepaymentPlanDateWithSecondMonthParamsShouldReturnNextTwoMonth() throws ParseException {
        String startDate = "01.12.2018";
        int nextMonthNum = 2;
        String expectedResult = "28.02.2019";

        repaymentRequest.setStartDate(startDate);
        repaymentPlanServices.setRepaymentRequest(repaymentRequest);
        String result = simpleDateFormat.format(repaymentPlanServices.getRepaymentPlanDate(nextMonthNum));

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void getRepaymentPlanDateWithZeroParamsShouldReturnLastDayOfSameMonth() throws ParseException {
        String startDate = "01.01.2019";
        int nextMonthNum = 0;
        String expectedResult = "31.01.2019";

        repaymentRequest.setStartDate(startDate);
        repaymentPlanServices.setRepaymentRequest(repaymentRequest);
        String result = simpleDateFormat.format(repaymentPlanServices.getRepaymentPlanDate(nextMonthNum));

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void getInterestWithParamsShouldReturnTrue() {
        double expectedInterest = 176.67;
        double result = repaymentPlanServices.getInterest(previousRepaymentPlan);

        Assert.assertEquals(expectedInterest, result, 2);
    }

    @Test
    public void getInterestWithZeroParamsShouldReturnZero() {
        double expectedInterest = 0;
        double result = repaymentPlanServices.getInterest(null);

        Assert.assertEquals(expectedInterest, result, 2);
    }

    @Test
    public void getFirstRepaymentShouldReturnValue() {
        double expectedInterest = 166.66;
        double result = repaymentPlanServices.getFirstRepayment();

        Assert.assertEquals(expectedInterest, result, 2);
    }

    @Test
    public void getRemainingDebtWithParamsShouldReturnValue() {
        double repayment = 166.66;
        double expectedInterest = -99833.34;
        double result = repaymentPlanServices.getRemainingDebt(previousRepaymentPlan, repayment);

        Assert.assertEquals(expectedInterest, result, 2);
    }
}
