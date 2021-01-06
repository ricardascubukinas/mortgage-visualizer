package com.dataStructure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AdvancedLoan extends Loan {
    private final IntegerProperty defermentFrom;
    private final IntegerProperty defermentLength;
    private final IntegerProperty filterFrom;
    private final IntegerProperty filterTo;

    public AdvancedLoan(int dfrom, int dlength, int ffrom, int fto, float balance, float interest, float interestType,
            int length, boolean type) {
        super(balance, interest, interestType, length, type);
        this.defermentFrom = new SimpleIntegerProperty(dfrom);
        this.defermentLength = new SimpleIntegerProperty(dlength);
        this.filterFrom = new SimpleIntegerProperty(ffrom);
        this.filterTo = new SimpleIntegerProperty(fto);
    }

    public void CalculateDefermentPeriod() {
        MonthlyPayment tempMonth = this.payments.get(this.getDefermentFrom() - 1);
        for (int i = 0; i < this.getDefermentLength(); i++) {
            this.payments.add(this.getDefermentFrom() + i - 1, new MonthlyPayment(tempMonth.getMonth() + i + 1,
                    tempMonth.getBalance(), tempMonth.getInterest(), tempMonth.getInterest(), 0));
        }

        this.setLoanLength(this.getLength() + this.getDefermentLength());
        this.filterTo.set(this.getLength());

        for (int i = 0; i < this.getLength(); i++) {
            this.payments.get(i).setMonth(i + 1);
        }
    }

    public void setFilterFrom(int month) {
        this.filterFrom.set(month);
    }

    public void setFilterTo(int month) {
        this.filterTo.set(month);
    }

    public void setDefermentFrom(int month) {
        this.defermentFrom.set(month);
    }

    public void setDefermentLength(int month) {
        this.defermentLength.set(month);
    }

    public void setLoanLength(int month) {
        this.length.set(month);
    }

    public int getDefermentFrom() {
        return defermentFrom.get();
    }

    public int getDefermentLength() {
        return defermentLength.get();
    }

    public int getFilterFrom() {
        return filterFrom.get();
    }

    public int getFilterTo() {
        return filterTo.get();
    }

}
