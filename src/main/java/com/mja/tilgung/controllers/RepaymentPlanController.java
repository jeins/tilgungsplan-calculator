package com.mja.tilgung.controllers;

import com.mja.tilgung.models.RepaymentPlan;
import com.mja.tilgung.models.RepaymentRequest;
import com.mja.tilgung.models.RepaymentSummary;
import com.mja.tilgung.services.RepaymentPlanServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repayment")
public class RepaymentPlanController {

    @GetMapping
    public boolean test(){
        return true;
    }

    @PostMapping
    public List<RepaymentPlan> repaymentPlanList (@RequestBody RepaymentRequest repaymentRequest) {
        RepaymentPlanServices repaymentPlanServices = new RepaymentPlanServices();

        repaymentPlanServices.setRepaymentRequest(repaymentRequest);
        List<RepaymentPlan> repaymentPlanList = repaymentPlanServices.generateRepaymentPlan();

        return repaymentPlanList;
    }

    @PostMapping
    @RequestMapping(value = "/summary")
    public RepaymentSummary repaymentPlanWithSummary(@RequestBody RepaymentRequest repaymentRequest) {
        RepaymentPlanServices repaymentPlanServices = new RepaymentPlanServices();
        repaymentPlanServices.setRepaymentRequest(repaymentRequest);

        RepaymentSummary repaymentSummary = new RepaymentSummary();
        repaymentSummary.setMonthlyPlans(repaymentPlanServices.generateRepaymentPlan());
        repaymentSummary.setTotalInterest(repaymentPlanServices.getTotalInterest());
        repaymentSummary.setRemainingDebt(repaymentPlanServices.getRemainingDebt());
        repaymentSummary.setTotalRepayment(repaymentPlanServices.getTotalRepayment());
        repaymentSummary.setTotalRate(repaymentPlanServices.getTotalRate());

        return repaymentSummary;
    }
}
