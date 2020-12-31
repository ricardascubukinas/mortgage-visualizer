package com.dataStructure;

import java.text.DecimalFormat;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Loan {
    protected final FloatProperty balance;
    protected final FloatProperty interestRate;
    protected final IntegerProperty length;
    // 0 - for annuity, 1 - for linear
    protected final BooleanProperty loanType;

    protected ObservableList<MonthlyPayment> payments = FXCollections.observableArrayList();;

    public Loan(float balance, float interest, int length, boolean type) {
        this.balance = new SimpleFloatProperty(balance);
        this.interestRate = new SimpleFloatProperty(interest);
        this.length = new SimpleIntegerProperty(length);
        this.loanType = new SimpleBooleanProperty(type);
        if (!loanType.get())
            CalculatePaymentsAnnuity();
        else
            CalculatePaymentsLinear();
    }

    public void CalculatePaymentsAnnuity() {
        DecimalFormat df;
        float tempBalance, installment, tempInterest, tempRepayment;
        float monthlyInterest;
        tempBalance = getBalance();
        monthlyInterest = getInterestRate() / 12 / 100;
        installment = (float) (Math
                .round((tempBalance / ((1 - Math.pow((1 + monthlyInterest), -getLength())) / monthlyInterest)) * 100.00)
                / 100.00);
        for (int i = 0; i < getLength(); i++) {
            tempInterest = (float) (Math.round(tempBalance * monthlyInterest * 100.00) / 100.00);
            tempRepayment = (float) (Math.round((installment - tempInterest) * 100.00) / 100.00);
            tempBalance = (float) (Math.round((tempBalance - tempRepayment) * 100.00) / 100.00);
            if (i == getLength() - 1) {
                tempBalance = 0.00f;
            }
            MonthlyPayment month = new MonthlyPayment(tempBalance, installment, tempInterest, tempRepayment);
            addPayment(month);
            System.out.println(month.getBalance());
            System.out.println(month.getInstallment());
            System.out.println(month.getInterest());
            System.out.println(month.getRepayment());
            System.out.println("\n");
        }

        // MonthlyPayment tempPayment = new MonthlyPayment(tempBalance, tempInstallment,
        // tempInterest, tempRepayment);
    }

    public void CalculatePaymentsLinear() {

    }

    public void addPayment(MonthlyPayment payment) {
        payments.add(payment);
    }

    public float getBalance() {
        return balance.get();
    }

    public float getInterestRate() {
        return interestRate.get();
    }

    public int getLength() {
        return length.get();
    }

    public boolean getLoanType() {
        return loanType.get();
    }

}
