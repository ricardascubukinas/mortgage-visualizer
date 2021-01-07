package com.dataStructure;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.dataStructure.MonthlyPayment;

@XmlRootElement(name = "payments")
public class PaymentsWrapper {

    private List<MonthlyPayment> payments;

    @XmlElement(name = "month")
    public List<MonthlyPayment> getPayments() {
        return payments;
    }

    public void setPersons(List<MonthlyPayment> payments) {
        this.payments = payments;
    }
}
