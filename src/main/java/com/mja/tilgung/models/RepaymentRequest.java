package com.mja.tilgung.models;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class RepaymentRequest {
    private Date startDate;
    private double loan;
    private double interestRate;
    private int durationFixedInterest;
    private double initialRepayment;

    public void setStartDate(String startDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        this.startDate = format.parse(startDate);
    }
}
