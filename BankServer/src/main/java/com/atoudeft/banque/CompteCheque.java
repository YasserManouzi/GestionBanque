package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire{
    /**
     * Crée un compte bancaire.
     *
     * @param numero numéro du compte
     * @param type   type du compte
     */
    public CompteCheque(String numero, TypeCompte type) {
        super(numero, type);
    }

    @Override
    public boolean crediter(double montant) {
        if(montant > 0 && montant <= solde) {
            solde = solde + montant;
            return true;
        }
            return false;


    }

    @Override
    public boolean debiter(double montant) {
        if(montant > 0 && montant <= solde) {
            solde = solde - montant;
            return true;
        }
        return false;
    }

    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        if(montant <= solde && montant>0){
            solde = solde - montant;
            System.out.println("Détails de la facture");
            System.out.println("Numéro de facture: " + numeroFacture + "\n" +
                     "Le montant à payer: " + montant + "\n" +
                     "Description de la facture" + description);
            return true;
        }
        return false;
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }
}
