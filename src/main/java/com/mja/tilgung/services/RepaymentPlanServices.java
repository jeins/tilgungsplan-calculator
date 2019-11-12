package com.mja.tilgung.services;

import com.mja.tilgung.models.RepaymentPlan;
import com.mja.tilgung.models.RepaymentRequest;
import com.mja.tilgung.utils.NumbersHelper;
import lombok.Getter;
import java.util.*;

public class RepaymentPlanServices {
    private RepaymentRequest repaymentRequest;
    @Getter
    private double totalInterest = 0;
    @Getter
    private double totalRepayment = 0;
    @Getter
    private double totalRate = 0;
    @Getter
    private double remainingDebt = 0;

    public void setRepaymentRequest(RepaymentRequest repaymentRequest) {
        this.repaymentRequest = repaymentRequest;
    }

    public List<RepaymentPlan> generateRepaymentPlan() {
        int totalMonths = repaymentRequest.getDurationFixedInterest() * 12 + 1;
        List<RepaymentPlan> repaymentPlans = new ArrayList<>();

        repaymentPlans.add(getFirstMonthRepaymentPlan());

        double interest = getInterest(getFirstMonthRepaymentPlan());
        double firstRepayment = getFirstRepayment();
        double rate = NumbersHelper.round(getMonthlyRate(interest, firstRepayment));

        for(int i=1; i < totalMonths; i++) {
            RepaymentPlan repaymentPlan = new RepaymentPlan();

            RepaymentPlan previousMonthRepayment = repaymentPlans.get(i - 1);

            interest = NumbersHelper.round(getInterest(previousMonthRepayment));
            double repayment = NumbersHelper.round(getRepayment(rate, interest));
            double remainingDebt = NumbersHelper.round(getRemainingDebt(previousMonthRepayment, repayment));

            repaymentPlan.setDate(getRepaymentPlanDate(i));
            repaymentPlan.setRemainingDebt(remainingDebt);
            repaymentPlan.setInterest(interest);
            repaymentPlan.setRepayment(repayment);
            repaymentPlan.setRate(rate);

            updateSummary(interest, repayment, rate, remainingDebt);

            repaymentPlans.add(repaymentPlan);
        }

        return repaymentPlans;
    }

    public void updateSummary(double interest, double repayment, double rate, double remainingDebt) {
        totalInterest += interest;
        totalRepayment += repayment;
        totalRate += rate;
        this.remainingDebt = remainingDebt;
    }

    public RepaymentPlan getFirstMonthRepaymentPlan() {
        RepaymentPlan repaymentPlan = new RepaymentPlan();
        double loan = 0 - repaymentRequest.getLoan();

        repaymentPlan.setDate(getRepaymentPlanDate(0));
        repaymentPlan.setRemainingDebt(loan);
        repaymentPlan.setInterest(0);
        repaymentPlan.setRepayment(loan);
        repaymentPlan.setRate(loan);

        return repaymentPlan;
    }

    public Date getRepaymentPlanDate(int month) {
        Date startDate = repaymentRequest.getStartDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    public double getInterest(RepaymentPlan previousRepayment) {
        double interestMonthly = repaymentRequest.getInterestRate() / 12;
        double previousDebt = Optional.ofNullable(previousRepayment)
                .map(plan -> plan.getRemainingDebt())
                .orElse(0d);

        return Math.abs(previousDebt) * (interestMonthly / 100);
    }

    public double getFirstRepayment() {
        double initialRepayment = repaymentRequest.getInitialRepayment() / 12;
        double loan = repaymentRequest.getLoan();

        return loan * (initialRepayment / 100);
    }

    public double getRepayment(double rate, double currentInterest) {
        return rate - currentInterest;
    }

    public double getRemainingDebt(RepaymentPlan previousRepayment, double currentRepayment) {
        double previousRemainingDebt = Optional.ofNullable(previousRepayment)
                .map(plan -> plan.getRemainingDebt())
                .orElse(0d);;

        return previousRemainingDebt + currentRepayment;
    }

    public double getMonthlyRate(double currentInterest, double currentRepayment) {
        return currentInterest + currentRepayment;
    }
}
