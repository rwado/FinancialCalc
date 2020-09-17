package com.example.rwado.financialcalc;

import java.util.Date;

public class Bank {
    private String name;
    private Date[] outgoingTransfersTime;
    private Date[] incomingTransfersTime;

    public Bank(String name) {
        this.name = name;
    }

    public Date[] getOutgoingTransfersTime() {
        return outgoingTransfersTime;
    }

    public void setOutgoingTransfersTime(Date[] outgoingTransfersTime) {
        this.outgoingTransfersTime = outgoingTransfersTime;
    }

    public Date[] getIncomingTransfersTime() {
        return incomingTransfersTime;
    }

    public void setIncomingTransfersTime(Date[] incomingTransfersTime) {
        this.incomingTransfersTime = incomingTransfersTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}






