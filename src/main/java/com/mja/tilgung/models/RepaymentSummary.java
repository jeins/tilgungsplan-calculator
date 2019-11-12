package com.mja.tilgung.models;

import lombok.Data;

import java.util.List;

@Data
public class RepaymentSummary {
    private List<RepaymentPlan> monthlyPlans;
    private double remainingDebt;
    private double totalInterest;
    private double totalRepayment;
    private double totalRate;
}
