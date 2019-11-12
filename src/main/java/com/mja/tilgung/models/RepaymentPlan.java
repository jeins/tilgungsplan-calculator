package com.mja.tilgung.models;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class RepaymentPlan {
    private Date date;
    private double remainingDebt;
    private double interest;
    private double repayment;
    private double rate;

    public String getDate() {
        String dateFormat = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        return simpleDateFormat.format(date);
    }
}
