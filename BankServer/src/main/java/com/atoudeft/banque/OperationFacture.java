package com.atoudeft.banque;

import java.text.SimpleDateFormat;

public class OperationFacture extends Operation{
    private double montant;
    private String numeroFacture;
    private String description;

    public OperationFacture(double montant, String numeroFacture, String description) {
        super(TypeOperation.FACTURE);
        this.montant = montant;
        this.numeroFacture = numeroFacture;
        this.description = description;
    }

    public double getMontant() {
        return montant;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(getDate()) + " " + getType() + " " + montant +
                " Numéro facture: " + numeroFacture + " Description: " + description;
    }
}
