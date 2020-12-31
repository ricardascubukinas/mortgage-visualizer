package com.dataStructure;

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
    protected final FloatProperty interestType;
    protected final IntegerProperty length;
    protected final FloatProperty totalRepayment = new SimpleFloatProperty();
    // 0 - for annuity, 1 - for linear
    protected final BooleanProperty loanType;

    protected ObservableList<MonthlyPayment> payments = FXCollections.observableArrayList();;

    public Loan(float balance, float interest, float interestType, int length, boolean type) {
        this.balance = new SimpleFloatProperty(balance);
        this.interestRate = new SimpleFloatProperty(interest);
        this.interestType = new SimpleFloatProperty(interestType);
        this.length = new SimpleIntegerProperty(length);
        this.loanType = new SimpleBooleanProperty(type);
        if (!loanType.get())
            CalculatePaymentsAnnuity();
        else
            CalculatePaymentsLinear();
    }

    public void CalculatePaymentsAnnuity() {
        float tempBalance, installment, tempInterest, tempRepayment;
        float monthlyInterest;
        tempBalance = getBalance();
        monthlyInterest = getInterestRate() / 100 * interestType.get();
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
            addPayment(new MonthlyPayment(i + 1, tempBalance, installment, tempInterest, tempRepayment));
        }
        totalRepayment.set((float) (Math.round(installment * getLength() * 100.00) / 100.00));
    }

    public void CalculatePaymentsLinear() {
        float tempBalance, tempInstallment, tempInterest, repayment, monthlyInterest;
        tempBalance = getBalance();
        monthlyInterest = getInterestRate() / 100 * interestType.get();
        repayment = (float) (Math.round(tempBalance / getLength() * 100.00) / 100.00);
        for (int i = 0; i < getLength(); i++) {
            tempInterest = (float) (Math.round(tempBalance * monthlyInterest * 100.00) / 100.00);
            tempInstallment = (float) (Math.round((repayment + tempInterest) * 100.00) / 100.00);
            tempBalance = (float) (Math.round((tempBalance - repayment) * 100.00) / 100.00);
            if (i == getLength() - 1) {
                tempBalance = 0.00f;
            }
            addPayment(new MonthlyPayment(i + 1, tempBalance, tempInstallment, tempInterest, repayment));
            System.out.println(payments.get(i).getBalance());
            System.out.println(payments.get(i).getInstallment());
            System.out.println(payments.get(i).getInterest());
            System.out.println(payments.get(i).getRepayment());
            System.out.println("");
            totalRepayment.set((float) (Math.round((totalRepayment.get() + tempInstallment) * 100.00) / 100.00));
        }
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

    public float getTotalRepayment() {
        return totalRepayment.get();
    }

    public ObservableList<MonthlyPayment> getPayments() {
        return payments;
    }

}
