package com.atoudeft.banque;

import java.text.SimpleDateFormat;

public class OperationDepot extends Operation{

    double montantDepot;

    public OperationDepot(double montantDepot) {
        super(TypeOperation.DEPOT);
        this.montantDepot = montantDepot;
    }

    public double getMontantDepot() {
        return montantDepot;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(getDate()) + " " + getType() + " " + montantDepot;
    }
}
