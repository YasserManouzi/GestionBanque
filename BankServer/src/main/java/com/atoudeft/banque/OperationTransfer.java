package com.atoudeft.banque;

import java.text.SimpleDateFormat;

public class OperationTransfer extends Operation {
    private double montantTransfer;
    private String compteDestinataire;

    public OperationTransfer(double montantTransfer, String compteDestinataire) {
        super(TypeOperation.TRANSFER);
        this.montantTransfer = montantTransfer;
        this.compteDestinataire = compteDestinataire;
    }

    public double getMontantTransfer() {
        return montantTransfer;
    }

    public String getCompteDestinataire() {
        return compteDestinataire;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(getDate()) + " " + getType() + " " + montantTransfer +
                " Compte destinataire: " + compteDestinataire;
    }
}
