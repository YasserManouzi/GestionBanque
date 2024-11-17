package com.atoudeft.serveur;

import com.atoudeft.banque.*;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        ServeurBanque serveurBanque = (ServeurBanque)serveur;
        Banque banque;
        ConnexionBanque cnx;
        String msg, typeEvenement, argument, numCompteClient, nip, numeroFacture, description, numeroCompteReceveur;
        String[] t;
        double montant;
        CompteClient compteClient;

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;
            System.out.println("SERVEUR: Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            cnx.setTempsDerniereOperation(System.currentTimeMillis());
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des numéros de comptes-clients connectés :
                    cnx.envoyer("LIST " + serveurBanque.list());
                    break;
                /******************* COMMANDES DE GESTION DE COMPTES *******************/
                case "NOUVEAU": //Crée un nouveau compte-client :
                    if (cnx.getNumeroCompteClient()!=null) {
                        cnx.envoyer("NOUVEAU NO deja connecte");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("NOUVEAU NO");
                    }
                    else {
                        numCompteClient = t[0];
                        nip = t[1];

                        banque = serveurBanque.getBanque();
                        if (banque.ajouter(numCompteClient,nip)) {
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));
                            cnx.envoyer("NOUVEAU OK " + t[0] + " cree");

                        }
                        else
                            cnx.envoyer("NOUVEAU NO "+t[0]+" existe");
                    }
                    break;
                /******************* COMPTE ÉPARGNE *******************/
                case "EPARGNE":
                    String numeroGenererEpargne = CompteBancaire.genereNouveauNumero();
                    banque = serveurBanque.getBanque();

                    numCompteClient = cnx.getNumeroCompteClient();
                    CompteEpargne epargne = new CompteEpargne(numeroGenererEpargne, TypeCompte.EPARGNE, 0.05);
                    compteClient = banque.getCompteClient(numCompteClient);
                    if (compteClient == null) {
                        cnx.envoyer("EPARGNE NO");
                        break;
                    }
                    if (compteClient.ajouter(epargne)) {
                        cnx.envoyer("EPARGNE OK");
                    } else {
                        cnx.envoyer("EPARGNE NO");
                    }

                /******************* DEPOT DANS UN COMPTE *******************/
                case "DEPOT":
                    if (cnx.getNumeroCompteClient() == null) {
                        cnx.envoyer("DEPOT NO");
                        break;
                    }
                    argument = evenement.getArgument();
                    montant = Double.parseDouble(argument);
                    banque = serveurBanque.getBanque();
                    compteClient = banque.getCompteClient(cnx.getNumeroCompteClient());
                    if (compteClient == null) {
                        cnx.envoyer("DEPOT NO");
                        break;
                    }
                    numCompteClient = cnx.getNumeroCompteClient();
                    for (CompteBancaire compteBancaire : compteClient.getComptes()) {
                        if (compteBancaire.getNumero().equals(numCompteClient)) {
                            if (compteBancaire.crediter(montant)) {
                                cnx.envoyer("DEPOT OK - Nouveau solde : " + compteBancaire.getSolde());
                            } else {
                                cnx.envoyer("DEPOT NO");
                            }
                            break;
                        }
                    }
                    break;

                /******************* RETRAIT DANS UN COMPTE *******************/
                case "RETRAIT":
                    if (cnx.getNumeroCompteClient() == null) {
                        cnx.envoyer("RETRAIT NO");
                        break;
                    }
                    argument = evenement.getArgument();
                    montant = Double.parseDouble(argument);
                    banque = serveurBanque.getBanque();
                    compteClient = banque.getCompteClient(cnx.getNumeroCompteClient());
                    if (compteClient == null) {
                        cnx.envoyer("RETRAIT NO");
                        break;
                    }
                    numCompteClient = cnx.getNumeroCompteClient();
                    for (CompteBancaire compteBancaire : compteClient.getComptes()) {
                        if (compteBancaire.getNumero().equals(numCompteClient)) {
                            if (compteBancaire.debiter(montant)) {
                                cnx.envoyer("RETRAIT OK - Nouveau solde : " + compteBancaire.getSolde());
                            } else {
                                cnx.envoyer("RETRAIT NO");
                            }
                            break;
                        }
                    }
                    break;

                /******************* FACTURATION DU CLIENT *******************/
                case "FACTURE":
                    argument = evenement.getArgument();
                    t = argument.split(" ");
                    if (t.length<3) {
                        cnx.envoyer("FACTURE NO");
                    } else {
                        if (cnx.getNumeroCompteClient() == null) {
                            cnx.envoyer("FACTURE NO");
                            break;
                        }
                        numeroFacture = t[0];
                        montant = Double.parseDouble(t[1]);
                        description = t[2];

                        banque = serveurBanque.getBanque();
                        compteClient = banque.getCompteClient(cnx.getNumeroCompteClient());
                        if (compteClient == null) {
                            cnx.envoyer("FACTURE NO");
                            break;
                        }
                        numCompteClient = cnx.getNumeroCompteClient();
                        for (CompteBancaire compteBancaire : compteClient.getComptes()) {
                            if (compteBancaire.getNumero().equals(numCompteClient)) {
                                if (compteBancaire.payerFacture(numeroFacture, montant, description)) {
                                    cnx.envoyer("FACTURE OK - Nouveau solde : " + compteBancaire.getSolde());
                                } else {
                                    cnx.envoyer("FACTURE NO");
                                }
                                break;
                            }
                        }
                        break;
                    }

                /******************* TRANSFERT *******************/
                case "TRANSFER":
                    argument = evenement.getArgument();
                    t = argument.split(" ");
                    if (t.length<2) {
                        cnx.envoyer("TRANSFER NO");
                    } else {
                        if (cnx.getNumeroCompteClient() == null) {
                            cnx.envoyer("TRANSFER NO");
                            break;
                        }
                        montant = Double.parseDouble(t[0]);
                        numeroCompteReceveur = t[1];
                        banque = serveurBanque.getBanque();
                        compteClient = banque.getCompteClient(cnx.getNumeroCompteClient());
                        if (compteClient == null) {
                            cnx.envoyer("TRANSFER NO");
                            break;
                        }
                        numCompteClient = cnx.getNumeroCompteClient();
                        for (CompteBancaire compteBancaire : compteClient.getComptes()) {
                            if (compteBancaire.getNumero().equals(numCompteClient)) {
                                if (compteBancaire.transferer(montant, numeroCompteReceveur)) {
                                    cnx.envoyer("TRANSFERT OK - Nouveau solde : " + compteBancaire.getSolde());
                                    CompteClient compteClientReceveur = banque.getCompteClient(cnx.getNumeroCompteClient());
                                    for (CompteBancaire compteBancaireReceveur : compteClientReceveur.getComptes()) {
                                        if (compteBancaireReceveur.getNumero().equals(numeroCompteReceveur)) {
                                            compteBancaireReceveur.crediter(montant);
                                            cnx.envoyer("TRANSFER OK");
                                        } else {
                                            cnx.envoyer("TRANSFER NO");
                                        }
                                    }
                                } else {
                                    cnx.envoyer("TRANSFERT NO");
                                }
                                break;
                            }
                        }
                    }

                /******************* CONNEXION À UN COMPTE *******************/
                case "CONNECT":
                    boolean Connecte = false;
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("NOUVEAU NO");
                    }
                    else {
                    numCompteClient = t[0];

                    for (Object connexion : serveurBanque.connectes) {
                        ConnexionBanque connexionBanque = (ConnexionBanque) connexion;

                        if (connexionBanque.getNumeroCompteClient().equals(numCompteClient)) {
                            Connecte = true;
                            cnx.envoyer("CONNECT OK");
                            break;
                        } else {
                            cnx.envoyer("CONNECT NO");
                            break;
                        }
                    }

                    if (Connecte) {
                        System.out.println("Le client est déjà connecté.");
                    } else {
                        System.out.println("Nouvelle connexion établie.");
                    }
                    break;
                    }

                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}