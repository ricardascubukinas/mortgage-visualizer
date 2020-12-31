package com.dataStructure;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class MonthlyPayment {
    private final FloatProperty balance;
    private final FloatProperty installment;
    private final FloatProperty interest;
    private final FloatProperty repayment;

    public MonthlyPayment(float balance, float installment, float interest, float repayment) {
        this.balance = new SimpleFloatProperty(balance);
        this.installment = new SimpleFloatProperty(installment);
        this.interest = new SimpleFloatProperty(interest);
        this.repayment = new SimpleFloatProperty(repayment);
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
