package com.dataStructure;

import java.io.Serializable;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MonthlyPayment implements Serializable {
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

    public void setMonth(int month) {
        this.month.set(month);
    }

    public void setBalance(float balance) {
        this.balance.set(balance);
    }

    public void setInstallment(float installment) {
        this.installment.set(installment);
    }

    public void setInterest(float interest) {
        this.interest.set(interest);
    }

    public void setRepayment(float repayment) {
        this.repayment.set(repayment);
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
