package com.dataStructure;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import com.dataStructure.MonthlyPayment;

public class Loan {
    private final FloatProperty interestRate;
    private final IntegerProperty length;
    // 0 - for annuity, 1 - for linear
    private final BooleanProperty loanType;
    private final Pair<IntegerProperty, IntegerProperty> deferment;
    private final Pair<IntegerProperty, IntegerProperty> filter;

    private ObservableList<MonthlyPayment> payments;
}
