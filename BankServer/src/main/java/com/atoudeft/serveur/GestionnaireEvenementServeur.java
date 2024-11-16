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
        String msg, typeEvenement, argument, numCompteClient, nip, numeroCompteClient;
        String[] t;
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
                    try {
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
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                /******************* COMMANDE POUR UNE CONNECTION *******************/
                case "CONNECT":
                    boolean dejaConnecte = false;
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    numCompteClient = t[0];
                    nip = t[1];
                    System.out.println(nip + numCompteClient);

                    System.out.println("Tentative de connexion avec numéro de compte: " + numCompteClient + " et NIP: " + nip);

                    for (Object connexion : serveurBanque.connectes) {
                        ConnexionBanque connexionBanque = (ConnexionBanque) connexion;

                        System.out.println("Vérification de connexion existante pour le compte: " + connexionBanque.getNumeroCompteClient());

                        if (connexionBanque.getNumeroCompteClient().equals(numCompteClient)) {
                            System.out.println("Le client est déjà connecté avec le numéro de compte: " + numCompteClient);
                            cnx.envoyer("CONNECT NO");
                            dejaConnecte = true;
                            break;
                        }
                    }

                    if (!dejaConnecte) {
                        compteClient = serveurBanque.getBanque().getCompteClient(numCompteClient);

                        if (compteClient == null) {
                            System.out.println("Le compte n'existe pas pour le numéro de compte: " + numCompteClient);
                            cnx.envoyer("CONNECT NO");
                        } else if (!compteClient.getNip().equals(nip)) {
                            System.out.println("Le NIP est incorrect pour le numéro de compte: " + numCompteClient);
                            cnx.envoyer("CONNECT NO");
                        } else {
                            System.out.println("Connexion réussie pour le numéro de compte: " + numCompteClient);
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(serveurBanque.getBanque().getNumeroCompteParDefaut(numCompteClient));
                            cnx.envoyer("CONNECT OK");
                        }
                    }
                    break;

                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}