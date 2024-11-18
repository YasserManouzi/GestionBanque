package com.atoudeft.banque;

import java.text.SimpleDateFormat;

public class OperationRetrait extends Operation{

    private double montantRetrait;

    public OperationRetrait(double montantRetrait) {
        super(TypeOperation.RETRAIT);
        this.montantRetrait = montantRetrait;
    }

    public double getMontantRetrait() {
        return montantRetrait;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(getDate()) + " " + getType() + " " + montantRetrait;
    }
}
