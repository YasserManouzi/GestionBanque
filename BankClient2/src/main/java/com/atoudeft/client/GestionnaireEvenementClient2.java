package com.atoudeft.client;

import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;
import com.atoudeft.vue.PanneauHistorique;
import com.atoudeft.vue.PanneauPrincipal;
import com.programmes.MainFrame;

import javax.swing.*;
import java.awt.*;

public class GestionnaireEvenementClient2 implements GestionnaireEvenement {
    private Client client;
    private PanneauPrincipal panneauPrincipal;

    /**
     * Construit un gestionnaire d'événements pour un client.
     *
     * @param client Client Le client pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementClient2(Client client, PanneauPrincipal panneauPrincipal) {

        this.client = client;
        this.panneauPrincipal = panneauPrincipal;
        this.client.setGestionnaireEvenement(this);
    }
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        //Connexion cnx;
        String typeEvenement, arg, str;
        int i;
        String[] t;
        MainFrame fenetre;

        if (source instanceof Connexion) {
            //cnx = (Connexion) source;
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "END": //Le serveur demande de fermer la connexion
                    client.deconnecter(); //On ferme la connexion
                    break;
                /******************* CREATION et CONNEXION *******************/
//                case "HIST": //Le serveur a renvoyé
//                    panneauPrincipal.setVisible(true);
//                    JOptionPane.showMessageDialog(null,"Panneau visible");
//                    cnx.envoyer("LIST");
//                    arg = evenement.getArgument();
//                    break;
                case "OK":
                    panneauPrincipal.setVisible(true);
                    fenetre = (MainFrame)panneauPrincipal.getTopLevelAncestor();
                    fenetre.setTitle(MainFrame.TITRE);//+" - Connecté"
                    break;
                case "NOUVEAU":
                    arg = evenement.getArgument();
                    if (arg.trim().startsWith("NO")) {
                        JOptionPane.showMessageDialog(panneauPrincipal,"Nouveau refusé");
                    }
                    else {
                        panneauPrincipal.cacherPanneauConnexion();
                        panneauPrincipal.montrerPanneauCompteClient();
                        str = arg.substring(arg.indexOf("OK")+2).trim();
                        panneauPrincipal.ajouterCompte(str);
                    }
                    break;
                case "CONNECT":
                    arg = evenement.getArgument();
                    if (arg.trim().startsWith("NO")) {
                        JOptionPane.showMessageDialog(panneauPrincipal,"Connexion refusée");
                    }
                    else {
                        panneauPrincipal.cacherPanneauConnexion();
                        panneauPrincipal.montrerPanneauCompteClient();
                        str = arg.substring(arg.indexOf("OK")+2).trim();
                        t = str.split(":");
                        for (String s:t) {
                            panneauPrincipal.ajouterCompte(s.substring(0,s.indexOf("]")+1));
                        }
                    }
                    break;
                /******************* SÉLECTION DE COMPTES *******************/
                case "EPARGNE" :
                    arg = evenement.getArgument();
                    if (arg.length() > 3) {
                            panneauPrincipal.ajouterCompte(arg.trim());
                            JOptionPane.showMessageDialog(panneauPrincipal,
                                    "Compte épargne créé avec succès !" + arg,
                                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(panneauPrincipal,
                                "Erreur dans les données reçues.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "SELECT" :
                    arg = evenement.getArgument();
                    if (arg.startsWith("OK")) {
                        String[] data = arg.split(" ");
                        if (data.length >= 3) {
                            String solde = data[2];

                            try {
                                panneauPrincipal.getPanneauOperationsCompte().getLblSolde().setText("Solde : " + solde);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(panneauPrincipal,
                                        "Erreur : Le solde du compte n'est pas valide.",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(panneauPrincipal,
                                    "Erreur : Réponse du serveur incorrecte. Format des données invalide.",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (arg.startsWith("NO")) {
                        JOptionPane.showMessageDialog(panneauPrincipal,
                                "Impossible de sélectionner ce compte. Détails : ",
                                "Erreur", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                /******************* OPÉRATIONS BANCAIRES *******************/
                case "DEPOT" :
                    arg = evenement.getArgument();
                    JOptionPane.showMessageDialog(panneauPrincipal,"DEPOT "+arg);
                    if (arg.startsWith("OK")) {
                        String[] data = arg.split(" ");
                        if (data.length >= 2) {
                            String solde = data[1];
                            try {
                                Double.parseDouble(solde);
                                panneauPrincipal.getPanneauOperationsCompte().getLblSolde().setText("Solde : " + solde);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(panneauPrincipal,
                                        "Erreur : Le solde du compte n'est pas valide.",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(panneauPrincipal,
                        "Erreur : Réponse du serveur incorrecte. Format des données invalide.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default:
                    System.out.println("RECU : "+evenement.getType()+" "+evenement.getArgument());
            }
        }
    }
}