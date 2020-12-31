package com.dataStructure;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MonthlyPayment {
    private final IntegerProperty month;
    private final FloatProperty balance;
    private final FloatProperty installment;
    private final FloatProperty interest;
    private final FloatProperty repayment;

    public MonthlyPayment(int month, float balance, float installment, float interest, float repayment) {
        this.month = new SimpleIntegerProperty(month);
        this.balance = new SimpleFloatProperty(balance);
        this.installment = new SimpleFloatProperty(installment);
        this.interest = new SimpleFloatProperty(interest);
        this.repayment = new SimpleFloatProperty(repayment);
    }

    public int getMonth() {
        return month.get();
    }

    public float getBalance() {
        return balance.get();
    }

    public float getInstallment() {
        return installment.get();
    }

    public float getInterest() {
        return interest.get();
    }

    public float getRepayment() {
        return repayment.get();
    }
}
