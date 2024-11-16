package com.atoudeft.banque;

public class CompteEpargne extends CompteBancaire{
    /**
     * Crée un compte bancaire.
     *
     * @param numero numéro du compte
     * @param type   type du compte
     */

    private double limite = 1000;
    private double frais = 2;
    private double tauxInterets;
    public CompteEpargne(String numero, TypeCompte type, double tauxInterets) {
        super(numero, type);
        this.tauxInterets = tauxInterets;
    }

    @Override
    public boolean crediter(double montant) {
        if(montant > 0) {
            double solde = getSolde();
            solde += montant;
            return true;
        }
        return false;
    }

    @Override
    public boolean debiter(double montant) {
        double solde = getSolde();
        if(solde > 0 && solde < 1000){
            if(montant > 0) {
                solde -= montant - 2 ;
                return true;
            } else {
                return false;
            }
        } else {
            if(montant > 0) {
                solde -= montant;
                return true;
            } else {
                return false;
            }
        }
    }



    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }

    public double getTauxInterets() {
        return tauxInterets;
    }

    public void setTauxInterets(double tauxInterets) {
        this.tauxInterets = tauxInterets;
    }

    public void ajouterInterets(double interets) {
        double solde = getSolde();
        double interetsSolde = solde * interets;
        solde += interetsSolde;
    }
}
