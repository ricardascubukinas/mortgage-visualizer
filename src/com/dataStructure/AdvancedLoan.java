package com.dataStructure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AdvancedLoan extends Loan {
    private final IntegerProperty defermentFrom;
    private final IntegerProperty defermentTo;
    private final IntegerProperty filterFrom;
    private final IntegerProperty filterTo;

    public AdvancedLoan(int dfrom, int dto, int ffrom, int fto, float balance, float interest, float interestType,
            int length, boolean type) {
        super(balance, interest, interestType, length, type);
        this.defermentFrom = new SimpleIntegerProperty(dfrom);
        this.defermentTo = new SimpleIntegerProperty(dto);
        this.filterFrom = new SimpleIntegerProperty(ffrom);
        this.filterTo = new SimpleIntegerProperty(fto);
    }

    public int getDefermentFrom() {
        return defermentFrom.get();
    }

    public int getDefermentTo() {
        return defermentTo.get();
    }

    public int getFilterFrom() {
        return filterFrom.get();
    }

    public int getFilterTo() {
        return filterTo.get();
    }

}
