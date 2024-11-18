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
            solde = solde + montant;
            return true;
        }
        return false;
    }

    @Override
    public boolean debiter(double montant) {
        if(solde > 0 && solde < limite){
            if(montant > 0 && montant <= solde) {
                solde = solde - montant - frais;
                return true;
            } else {
                return false;
            }
        } else {
            if(montant > 0 && montant <= solde) {
                solde = solde - montant;
                return true;
            } else {
                return false;
            }
        }
    }



    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        if(solde > 0 && solde < limite){
            if(montant > 0 && montant <= solde) {
                solde = solde - montant - frais;
                return true;
            } else {
                return false;
            }
        } else {
            if(montant > 0 && montant <= solde) {
                solde = solde - montant;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        if(solde > 0 && solde < limite){
            if(montant > 0 && montant <= solde) {
                solde = solde - montant - frais;
                return true;
            } else {
                return false;
            }
        } else {
            if(montant > 0 && montant <= solde) {
                solde = solde - montant;
                return true;
            } else {
                return false;
            }
        }
    }

    public double getTauxInterets() {
        return tauxInterets;
    }

    public void setTauxInterets(double tauxInterets) {
        this.tauxInterets = tauxInterets;
    }

    public double ajouterInterets(double interets) {
        double interetsSolde = solde * interets;
        return solde = solde * interetsSolde;
    }
}
